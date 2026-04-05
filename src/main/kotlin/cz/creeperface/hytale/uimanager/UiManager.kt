@file:OptIn(ExperimentalAtomicApi::class, ExperimentalTime::class)

package cz.creeperface.hytale.uimanager

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.packets.interface_.*
import com.hypixel.hytale.server.core.asset.common.CommonAssetModule
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.ui.builder.EventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import cz.creeperface.hytale.uimanager.asset.DynamicAsset
import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.group
import cz.creeperface.hytale.uimanager.codec.CustomPageBuilderCodec
import cz.creeperface.hytale.uimanager.event.EventBinding
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.event.EventResponse
import cz.creeperface.hytale.uimanager.serializer.GenericNode
import cz.creeperface.hytale.uimanager.serializer.UiDiffProcessor
import cz.creeperface.hytale.uimanager.serializer.UiSerializer
import cz.creeperface.hytale.uimanager.special.FormResponse
import cz.creeperface.hytale.uimanager.special.UiForm
import cz.creeperface.hytale.uimanager.util.CustomHudHelper
import cz.creeperface.hytale.uimanager.util.getComponent
import cz.creeperface.hytale.uimanager.util.player
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

//class OpenPage {
//
//}

enum class UpdateMode {
    Manual,
    AutoRebind
}

class PageData(
    val assetName: String,
    val pagePath: String,
    val page: UiPage,
    val forms: List<UiForm<*>>,
    val initialDataClass: KClass<out Any>,
    val factory: (PlayerRef?, Any) -> UiPage
)

class DynamicPageData(
    val data: PageData,
    val factory: (PlayerRef?, Any) -> UiPage,
    val updateMode: UpdateMode,
    val minUpdateFrequency: Duration
)

class CustomHudInstance(
    val identifier: String,
    val hud: CustomUIHud,
    var firstRender: Boolean = true,
    var commands: List<CustomUICommand>? = null
)

class HudPageInstance(
    val pageId: String,
    var page: UiPage,
    var lastSentPage: UiPage,
    val pageData: PageData,
    val dynamicPageData: DynamicPageData?,
    var firstRender: Boolean = true,
    val scheduledCommands: MutableList<CustomUICommand>,
    var lastUpdate: Instant,
)

class PageInstance(
    val pageId: String,
    var page: UiPage,
    var lifeTime: CustomPageLifetime,
    var lastSentPage: UiPage,
    val pageData: PageData,
    var eventBindings: MutableMap<Int, EventBinding>,
    val userData: Any
)

object UiManager {
    const val ASSET_PATH = "UI/Custom/Pages/UiManager/"
    const val PAGE_PATH = "Pages/UiManager/"

    private var closed = false

    private val pageIdRegex = "^[a-zA-Z0-9]+$".toRegex()
    private val pageCounter = AtomicInteger(0)

    private val pages = mutableMapOf<String, PageData>()
    private val dynamicPages = mutableMapOf<String, DynamicPageData>()

    private val openPages = mutableMapOf<PlayerRef, PageInstance>()
    private val openHuds = mutableMapOf<PlayerRef, MutableMap<String, HudPageInstance>>()

    private val customHudInitializedPlayers = mutableSetOf<PlayerRef>()
    private val customHuds = mutableMapOf<PlayerRef, MutableMap<String, CustomHudInstance>>()

    private val updateLock = ReentrantReadWriteLock()
    private val scheduledUpdates = ConcurrentHashMap<PlayerRef, MutableSet<HudPageInstance>>()
    private val pendingHudRemovals = ConcurrentHashMap<PlayerRef, MutableSet<String>>()

    internal val firstSendPlayers = ConcurrentHashMap.newKeySet<PlayerRef>()

    private val GSON = GsonBuilder().create()

    fun registerStaticHud(pageId: String, pageFactory: ChildNodeBuilder.() -> Unit) {
        require(pageIdRegex.matches(pageId)) {
            "Page ID must be alphanumeric: '$pageId'"
        }

        require(!pages.containsKey(pageId)) {
            "Duplicated page ID: $pageId"
        }

        registerPageInternal(pageId, Unit, Unit::class) { _, _ ->
            customUi {
                group {
                    id = pageId
                    pageFactory()
                }
            }
        }
    }

    inline fun <reified T : Any> registerPage(
        pageId: String,
        initialData: T,
        noinline pageFactory: ChildNodeBuilder.(PlayerRef?, T) -> Unit
    ) = registerPage(pageId, initialData, T::class, pageFactory)

    fun <T : Any> registerPage(
        pageId: String,
        initialData: T,
        initialDataClass: KClass<out T>,
        pageFactory: ChildNodeBuilder.(PlayerRef?, T) -> Unit
    ) {
        require(pageIdRegex.matches(pageId)) {
            "Page ID must be alphanumeric: '$pageId'"
        }

        require(!pages.containsKey(pageId)) {
            "Duplicated page ID: $pageId"
        }

        registerPageInternal(pageId, initialData, initialDataClass, pageFactory)
    }

    inline fun <reified T : Any> registerDynamicHudWithManualUpdate(
        pageId: String,
        initialData: T,
        noinline pageFactory: ChildNodeBuilder.(PlayerRef?, T) -> Unit,
    ) = registerDynamicHudWithManualUpdate(pageId, initialData, T::class, pageFactory)

    fun <T : Any> registerDynamicHudWithManualUpdate(
        pageId: String,
        initialData: T,
        initialDataClass: KClass<out T>,
        pageFactory: ChildNodeBuilder.(PlayerRef?, T) -> Unit,
    ) = registerDynamicHud(pageId, pageFactory, initialData, initialDataClass, UpdateMode.Manual)

    fun registerDynamicHudWithAutoUpdate(
        pageId: String,
        pageFactory: ChildNodeBuilder.(PlayerRef?) -> Unit,
        minUpdateFrequency: Duration = 500.milliseconds
    ) = registerDynamicHud(
        pageId,
        { playerRef, _ -> pageFactory(playerRef) },
        Unit,
        Unit::class,
        UpdateMode.AutoRebind,
        minUpdateFrequency
    )

    private fun <T : Any> registerDynamicHud(
        pageId: String,
        pageFactory: ChildNodeBuilder.(PlayerRef?, T) -> Unit,
        initialData: T,
        initialDataClass: KClass<out T>,
        updateMode: UpdateMode,
        minUpdateFrequency: Duration = 500.milliseconds
    ) {
        require(pageIdRegex.matches(pageId)) {
            "Page ID must be alphanumeric: '$pageId'"
        }

        require(!pages.containsKey(pageId)) {
            "Duplicated page ID: $pageId"
        }

//        val page = customUi {
//            group {
//                id = pageId
//                pageFactory(null, initialData)
//            }
//        }

        val internalFactory = { playerRef: PlayerRef?, ctx: T ->
            customUi {
                group {
                    id = pageId
                    pageFactory(playerRef, ctx)
                }
            }
        }

        val pageData = registerPageInternal(pageId, initialData, initialDataClass, pageFactory)

        dynamicPages[pageId] = DynamicPageData(
            pageData,
            internalFactory as (PlayerRef?, Any?) -> UiPage,
            updateMode,
            minUpdateFrequency
        )
    }

    internal fun <T : Any> registerPageInternal(
        pageId: String,
        initialData: T,
        initialDataClass: KClass<out T>,
        pageFactory: ChildNodeBuilder.(PlayerRef?, T) -> Unit
    ): PageData {
        require(!closed) {
            "UiManager registry is closed. Register pages during plugin setup"
        }

        val internalFactory = { playerRef: PlayerRef?, ctx: T ->
            customUi {
                group {
                    id = pageId
                    pageFactory(playerRef, ctx)
                }
            }
        }

        val page = internalFactory(null, initialData).clone()
        val forms = extractPageForms(page)

        val serializedPage = UiSerializer.serialize(page)
        HytaleLogger.getLogger().atInfo().log("Serialized: $pageId")
        HytaleLogger.getLogger().atInfo().log(serializedPage)

        val fileName = "Page" + pageCounter.getAndIncrement() /*+ "_"*/ + pageId + ".ui"

        val assetName = ASSET_PATH + fileName
        HytaleLogger.getLogger().atInfo().log("Adding asset $assetName - file name: $fileName")
        val asset = DynamicAsset(assetName, serializedPage.toByteArray(Charsets.UTF_8))
        CommonAssetModule.get().addCommonAsset("UiManager", asset)

        @Suppress("UNCHECKED_CAST")
        val pageData = PageData(
            assetName,
            PAGE_PATH + fileName,
            page,
            forms,
            initialDataClass,
            internalFactory as (PlayerRef?, Any) -> UiPage
        )
        pages[pageId] = pageData

        return pageData
    }

    fun showStaticHud(pageId: String, playerRef: PlayerRef) {
        val pageData = requireNotNull(pages[pageId]) {
            "Page ID '$pageId' does not exist."
        }

        val openHuds = openHuds.computeIfAbsent(playerRef) { mutableMapOf() }

        require(!openHuds.containsKey(pageId)) {
            "Page ID '$pageId' is already shown to player ${playerRef.username}"
        }

        val pageInstance = HudPageInstance(
            pageId,
            pageData.page,
            pageData.page,
            pageData,
            null,
            true,
            mutableListOf(),
            Clock.System.now()
        )
        openHuds[pageId] = pageInstance

        HytaleLogger.getLogger().atInfo().log("Showing dynamic HUD with ID '$pageId' to player ${playerRef.username}")
        scheduleHudUpdate(playerRef, pageInstance)
    }

    fun showDynamicHud(pageId: String, playerRef: PlayerRef, context: Any = Unit) {
        val pageData = requireNotNull(pages[pageId]) {
            "Page ID '$pageId' does not exist."
        }
        val dynamicPageData = requireNotNull(dynamicPages[pageId]) {
            "Dynamic page ID '$pageId' does not exist."
        }

        require(dynamicPageData.data.initialDataClass.isInstance(context)) {
            "Context type ${context::class} is not compatible with expected type ${dynamicPageData.data.initialDataClass}"
        }

        val page = dynamicPageData.factory(playerRef, context)
        val sentPage = page.clone()

        val commandBuilder = UICommandBuilder()

        UiDiffProcessor.generateUpdateCommands(
            pageData.page,
            sentPage,
            commandBuilder
        )

        val openHuds = openHuds.computeIfAbsent(playerRef) { mutableMapOf() }

        require(!openHuds.containsKey(pageId)) {
            "Page ID '$pageId' is already shown to player ${playerRef.username}"
        }

        commandBuilder.commands.forEach { command ->
            HytaleLogger.getLogger().atInfo()
                .log("HUD Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
        }

        val pageInstance = HudPageInstance(
            pageId,
            page,
            sentPage,
            pageData,
            dynamicPageData,
            true,
            commandBuilder.commands.toMutableList(),
            Clock.System.now()
        )
        openHuds[pageId] = pageInstance

        scheduleHudUpdate(playerRef, pageInstance)
    }

    fun hideHud(pageId: String, playerRef: PlayerRef) {
        val removed = openHuds[playerRef]?.remove(pageId) ?: return

        updateLock.read {
            pendingHudRemovals.computeIfAbsent(playerRef) { mutableSetOf() }.add(pageId)
            scheduledUpdates.computeIfAbsent(playerRef) { mutableSetOf() }
        }
    }

    fun showPage(
        playerRef: PlayerRef,
        pageId: String,
        userData: Any,
        lifetime: CustomPageLifetime = CustomPageLifetime.CanDismissOrCloseThroughInteraction,
        formData: List<Any?> = emptyList()
    ) {
        val pageManager = playerRef.player.pageManager
        val ref = playerRef.reference ?: return

        val pageData = pages[pageId]!!

        require(pageData.initialDataClass.isInstance(userData)) {
            "Context type ${userData::class} is not compatible with expected type ${pageData.initialDataClass}"
        }

        val interactivePage = object : InteractiveCustomUIPage<Any>(playerRef, lifetime, CustomPageBuilderCodec()) {
            override fun build(
                ref: Ref<EntityStore>,
                commandBuilder: UICommandBuilder,
                eventBuilder: UIEventBuilder,
                store: Store<EntityStore>
            ) {
                commandBuilder.append(pageData.pagePath)

                val sentPage = pageData.factory(playerRef, userData)

                extractPageForms(sentPage)
                val eventBindings = addEventBindings(sentPage, eventBuilder)

                UiDiffProcessor.generateUpdateCommands(
                    pageData.page,
                    sentPage,
                    commandBuilder
                )

                commandBuilder.commands.forEach { command ->
                    HytaleLogger.getLogger().atInfo().log("Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
                }

                processPageForms(pageData, commandBuilder, eventBuilder, formData)

                openPages[playerRef] = PageInstance(
                    pageId,
                    pageData.page,
                    lifetime,
                    sentPage,
                    pageData,
                    eventBindings.toMutableMap(),
                    userData,
                )
            }

            override fun handleDataEvent(ref: Ref<EntityStore>, store: Store<EntityStore>, rawData: String) {
                HytaleLogger.getLogger().atInfo().log("handleDataEvent: $rawData")
                val json = JsonParser.parseString(rawData)

                if (json is JsonObject && json.has("FormIndex")) {
                    val response = try {
                        FormResponse.fromJson(json)
                    } catch (e: Exception) {
                        HytaleLogger.getLogger().atInfo().withCause(e).log("Failed to parse form response")
                        return
                    }

                    handleFormSubmit(playerRef, pageData, response)
                    return
                }

                val eventResponse = try {
                    EventResponse.fromJson(json)
                } catch (e: Exception) {
                    HytaleLogger.getLogger().atInfo().withCause(e).log("Failed to parse response")
                    return
                }
                HytaleLogger.getLogger().atInfo().log("response: $eventResponse")

                eventResponse.values.forEach { string, any ->
                    HytaleLogger.getLogger().atInfo().log("key: $string, value type: ${any?.javaClass?.simpleName}, value: $any")
                }

                handlePageEvent(playerRef, pageData, eventResponse)
            }

            override fun onDismiss(ref: Ref<EntityStore>, store: Store<EntityStore>) {
                val playerRef = ref.getComponent(PlayerRef.getComponentType()) ?: return

                openPages.remove(playerRef)
            }
        }

        pageManager.openCustomPage(
            ref,
            ref.store,
            interactivePage
        )
    }

    private fun handlePageEvent(playerRef: PlayerRef, pageData: PageData, response: EventResponse) {
        val pageInstance = openPages[playerRef] ?: return
        val eventBinding = pageInstance.eventBindings[response.eventId] ?: return

        val ctx = EventContext(playerRef, response, response.values)

        eventBinding.action(ctx)
    }

    private fun handleFormSubmit(playerRef: PlayerRef, pageData: PageData, response: FormResponse) {
        val form = pageData.forms.getOrNull(response.formIndex)

        if (form == null) {
            HytaleLogger.getLogger().atInfo().log("Form not found for index ${response.formIndex}")
            return
        }

        @Suppress("UNCHECKED_CAST")
        val castedForm = form as UiForm<Any>
        val submitHandler = castedForm.submitHandler

        if (submitHandler == null) {
            HytaleLogger.getLogger().atInfo().log("Submit handler not found for form ${response.formIndex}")
            return
        }

        val boundClass = castedForm.boundClass
        val boundClassConstructor = requireNotNull(boundClass.primaryConstructor) {
            "Primary constructor not found for bound class ${boundClass.qualifiedName}"
        }

        val formData = boundClassConstructor.callBy(emptyMap())

        castedForm.boundProperties.forEachIndexed { index, boundPropertyEntry ->
            val value = response.values["@Value$index"] ?: return@forEachIndexed
            val property = boundPropertyEntry.boundProperty

            // Basic type conversion
            val convertedValue: Any? = when (property.returnType.classifier) {
                Int::class -> value.toIntOrNull()
                Boolean::class -> value.toBoolean()
                String::class -> value
                Double::class -> value.toDoubleOrNull()
                Float::class -> value.toFloatOrNull()
                Long::class -> value.toLongOrNull()
                else -> value
            }

            if (convertedValue != null) {
                property.set(formData, convertedValue)
            }
        }

        submitHandler(playerRef, formData)
    }

    fun updatePage(
        playerRef: PlayerRef,
        pageId: String,
        userData: Any = Unit,
        clear: Boolean = false,
        formData: List<Any?> = emptyList()
    ) {
        val openPage = openPages[playerRef] ?: return

        if (openPage.pageId != pageId) {
            return
        }

        val newPage = openPage.pageData.factory(playerRef, userData)
        extractPageForms(newPage)

        val commandBuilder = UICommandBuilder()
        val eventBuilder = UIEventBuilder()

        UiDiffProcessor.generateUpdateCommands(openPage.lastSentPage, newPage, commandBuilder)
        val eventBindings = addEventBindings(newPage, eventBuilder)

        processPageForms(openPage.pageData, commandBuilder, eventBuilder, formData)

        commandBuilder.commands.forEach { command ->
            HytaleLogger.getLogger().atInfo().log("Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
        }

        playerRef.packetHandler.writeNoCache(CustomPage(
            pageId,
            false,
            clear,
            openPage.lifeTime,
            commandBuilder.commands,
            eventBuilder.events
        ))

        openPage.eventBindings = eventBindings.toMutableMap()
        openPage.lastSentPage = newPage
    }

    private fun scheduleHudUpdate(playerRef: PlayerRef, pageInstance: HudPageInstance) {
        updateLock.read {
            val playerUpdates = scheduledUpdates.computeIfAbsent(playerRef) { mutableSetOf() }
            playerUpdates.add(pageInstance)
        }
    }

    private fun updateHudPageInstance(playerRef: PlayerRef, pageInstance: HudPageInstance) {
        val commandBuilder = UICommandBuilder()

        pageInstance.page.resetDirty()
        val sendPage = pageInstance.page.clone() //TODO: get real changes instead of full clone

        UiDiffProcessor.generateUpdateCommands(
            pageInstance.lastSentPage,
            sendPage,
            commandBuilder
        )

        commandBuilder.commands.forEach { command ->
            HytaleLogger.getLogger().atInfo()
                .log("HUD Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
        }

        pageInstance.lastSentPage = sendPage

        pageInstance.scheduledCommands.clear()
        pageInstance.scheduledCommands.addAll(commandBuilder.commands)

        scheduleHudUpdate(playerRef, pageInstance)
    }

    private fun checkForAutoUpdate() {
        val now = Clock.System.now()

        openHuds.forEach { (playerRef, openPages) ->
            openPages.values.forEach { pageInstance ->
                pageInstance.dynamicPageData?.let { dynamicPageData ->
                    if (dynamicPageData.updateMode === UpdateMode.Manual) {
                        return@forEach
                    }

                    if (!pageInstance.page.isDirty) {
                        return@forEach
                    }

                    val sinceLastUpdate = now - pageInstance.lastUpdate

                    if (sinceLastUpdate < dynamicPageData.minUpdateFrequency) {
                        return@forEach
                    }

                    if (pageInstance.page.isDirty) {
                        updateHudPageInstance(playerRef, pageInstance)
                    }
                }
            }
        }
    }

    private fun processPlayerUpdates() {
        val scheduledUpdates = updateLock.write {
//            this.scheduledUpdates.extract { playerRef, _ ->
//                playerRef.worldUuid != null && subscribedPlayers.contains(playerRef)
//            }
            val updates = this.scheduledUpdates.toMap()
            this.scheduledUpdates.clear()
            updates
        }

        val hudRemovals = updateLock.write {
            val removals = this.pendingHudRemovals.toMap()
            this.pendingHudRemovals.clear()
            removals
        }
        scheduledUpdates.forEach { (playerRef, instances) ->
            val commands = mutableListOf<CustomUICommand>()
            val eventBuilder = UIEventBuilder()

            val pagesWithUpdate = instances.associate { instance ->
                val entry = instance.pageId to instance.scheduledCommands.toList()
                instance.scheduledCommands.clear()

                entry
            }

            val firstRender = !customHudInitializedPlayers.contains(playerRef)

            if (firstRender) {
                customHudInitializedPlayers.add(playerRef)

                commands.add(
                    CustomUICommand(
                        CustomUICommandType.AppendInline,
                        null,
                        null,
                        "Group #MultipleHUD {}",
                    )
                )
            }

            openHuds[playerRef]?.forEach { (pageId, instance) ->
                if (instance.firstRender) {
                    instance.firstRender = false

                    val appendCommand = CustomUICommand(
                        CustomUICommandType.Append,
                        null,
                        null,
                        instance.pageData.pagePath
                    )
                    commands.add(appendCommand)
                }

                pagesWithUpdate[pageId]?.forEach { pageUpdateCommand ->
                    commands.add(pageUpdateCommand)
                }
            }

            hudRemovals[playerRef]?.forEach { pageId ->
                commands.add(CustomUICommand(CustomUICommandType.Clear, "#$pageId", null, null))
            }

            val customHuds = synchronized(customHuds) {
                val playerHuds = customHuds[playerRef]
                val customHuds = playerHuds?.toMap()
                playerHuds?.clear()

                customHuds
            }

            customHuds?.forEach { (_, hudInstance) ->
                val customHudGroupId = "#${hudInstance.identifier}"

                val customHudCommands = hudInstance.commands ?: run {
                    val commands = CustomHudHelper.build(hudInstance.hud, "#MultipleHUD $customHudGroupId")
                    hudInstance.commands = commands

                    commands
                }

                if (hudInstance.firstRender) {
                    hudInstance.firstRender = false
                    commands.add(CustomUICommand(CustomUICommandType.AppendInline, "#MultipleHUD", null, "Group $customHudGroupId {}"))
                } else {
                    commands.add(CustomUICommand(CustomUICommandType.Clear, "#MultipleHUD $customHudGroupId", null, null))
                }

                commands.addAll(customHudCommands)
            }

            if (commands.isNotEmpty()) {
                if (firstRender) {
                    firstSendPlayers.add(playerRef)
                }

                playerRef.packetHandler.writeNoCache(
                    CustomHud(
                        firstRender,
                        commands.toTypedArray()
                    )
                )

                if (firstRender) {
                    firstSendPlayers.remove(playerRef)
                }
            }
        }
    }

    internal fun update() {
        checkForAutoUpdate() //TODO: set check frequency in config
        processPlayerUpdates()
    }

    fun update(playerRef: PlayerRef, pageId: String, context: Any) {
        val pageInstance = openHuds[playerRef]?.get(pageId) ?: return
        val dynamicPageData = requireNotNull(pageInstance.dynamicPageData) {
            "Trying to update page ID '$pageId' which is not dynamic"
        }

        pageInstance.page = dynamicPageData.factory(playerRef, context)

        updateHudPageInstance(playerRef, pageInstance)
    }

    internal fun onPlayerDisconnect(playerRef: PlayerRef) {
        openHuds.remove(playerRef)
        openPages.remove(playerRef)
        pendingHudRemovals.remove(playerRef)
    }

    private fun extractPageForms(page: UiPage): List<UiForm<*>> {
        val forms = mutableListOf<UiForm<*>>()

        fun processNode(node: UiNode): List<UiNode> {
            HytaleLogger.getLogger().atInfo().log("Node ${node::class.simpleName}")
            val nodes: MutableList<UiNode> = mutableListOf()

            when (node) {
                is UiForm<*> -> {
                    HytaleLogger.getLogger().atInfo().log("Form children: ${node.children.size}")
                    node.children.forEach { child ->
                        nodes.addAll(processNode(child))
                    }

                    forms.add(node)
                }

                is UiNodeWithChildren -> {
                    val reorderedChildren = mutableListOf<UiNode>()

                    node.children.forEach { child ->
                        reorderedChildren.addAll(processNode(child))
                    }

                    node.children.clear()
                    node.children.addAll(reorderedChildren)

                    nodes.add(node)
                }

                else -> {
                    nodes.add(node)
                }
            }

            return nodes
        }

        val reorderedNodes = mutableListOf<UiNode>()
        page.nodes.forEach { node ->
            reorderedNodes.addAll(processNode(node))
        }

        page.nodes.clear()
        page.nodes.addAll(reorderedNodes)

        return forms
    }

    private fun addEventBindings(page: UiPage, eventBuilder: UIEventBuilder): Map<Int, EventBinding> {
        val eventBindings = getEventBindings(page)
        val result = mutableMapOf<Int, EventBinding>()
        eventBindings.forEach { binding ->
            val eventId = addEventBinding(binding, eventBuilder)
            result[eventId] = binding
        }

        return result
    }

    private fun addEventBindings(node: GenericNode, eventBuilder: UIEventBuilder): Map<Int, EventBinding> {
        val source = node.source
        val eventBindings = mutableListOf<EventBinding>()

        if (source is UiNode) {
            eventBindings.addAll(source.getEventBindings())

            if (source is UiNodeWithChildren) {
                eventBindings.addAll(getEventBindings(source))
            }
        }

        val result = mutableMapOf<Int, EventBinding>()
        eventBindings.forEach { binding ->
            val eventId = addEventBinding(binding, eventBuilder)
            result[eventId] = binding
        }
        return result
    }

    private fun addEventBinding(eventBinding: EventBinding, eventBuilder: UIEventBuilder): Int {
        val eventData = EventData()
        val eventId = eventIdCounter.getAndIncrement()
        eventData.append("EventId", eventId.toString())

        eventBinding.boundProperties.forEach { boundPropertyEntry ->
            val propertyPath = boundPropertyEntry.nodeInstance.id + "." + boundPropertyEntry.nodeProperty.name.replaceFirstChar {
                it.uppercase()
            }

            eventData.append(
                "@$propertyPath",
                "#$propertyPath",
            )
        }

        eventBuilder.addEventBinding(
            eventBinding.type,
            "#" + eventBinding.nodeId,
            eventData,
            false
        )

        return eventId
    }

    private val eventIdCounter = AtomicInteger(0)

    private fun getEventBindings(node: UiNodeWithChildren): List<EventBinding> {
        val eventBindings = mutableListOf<EventBinding>()

        node.children.forEach { child ->
            eventBindings.addAll(child.getEventBindings())

            if (child is UiNodeWithChildren) {
                eventBindings.addAll(getEventBindings(child))
            }
        }

        return eventBindings
    }

    private fun processPageForms(
        pageData: PageData,
        commandBuilder: UICommandBuilder,
        eventBuilder: UIEventBuilder?,
        formsData: List<Any?>
    ) {
        fun processForm(formIndex: Int, form: UiForm<*>) {
            val formData = formsData.getOrNull(formIndex)

            if (formData != null) {
                require(formData::class == form.boundClass) {
                    "Form data class ${formData::class.simpleName} does not match bound class ${form.boundClass.simpleName} for form at index $formIndex"
                }

                @Suppress("UNCHECKED_CAST")
                form as UiForm<Any>

                form.boundProperties.forEach { boundPropertyEntry ->
                    commandBuilder.set(
                        "#" + boundPropertyEntry.nodeInstance.id,
                        boundPropertyEntry.boundProperty.get(formData).toString()
                    )
                }
            }

            if (eventBuilder != null) {
                form.submitters.forEachIndexed { submitterIndex, submitter ->
                    val eventData = EventData()
                    eventData.append("FormIndex", formIndex.toString())
                    eventData.append("FormSubmitterIndex", submitterIndex.toString())

                    form.boundProperties.forEachIndexed { index, boundPropertyEntry ->
                        val propertyPath =
                            "#" + boundPropertyEntry.nodeInstance.id + "." + boundPropertyEntry.nodeProperty.name.replaceFirstChar {
                                it.uppercase()
                            }

                        eventData.append(
                            "@Value$index",
                            propertyPath
                        )
                    }

                    eventBuilder.addEventBinding(
                        submitter.eventType,
                        "#" + submitter.node.id,
                        eventData,
                        false
                    )
                }
            }
        }

        pageData.forms.forEachIndexed { formIndex, form ->
            processForm(formIndex, form)
        }
    }

    internal fun addCustomUiHud(player: PlayerRef, identifier: String, hud: CustomUIHud) {
        HytaleLogger.getLogger().atInfo().log("Adding custom hud $identifier for player ${player.username}")

        val playerHuds = synchronized(customHuds) {
            customHuds
                .getOrPut(player) { mutableMapOf() }
        }

        val update = playerHuds.containsKey(identifier)
        playerHuds[identifier] = CustomHudInstance(identifier, hud, !update)

        updateLock.read {
            scheduledUpdates.getOrPut(player) { mutableSetOf() }
        }

        if (!update) {
            processPlayerUpdates()
        }
    }

    internal fun removeCustomUiHud(player: PlayerRef, identifier: String) {
        HytaleLogger.getLogger().atInfo().log("Removing custom hud $identifier from player ${player.username}")

        synchronized(customHuds) {
            customHuds[player]?.remove(identifier)
        }

        updateLock.read {
            scheduledUpdates.getOrPut(player) { mutableSetOf() }
        }
    }
}
