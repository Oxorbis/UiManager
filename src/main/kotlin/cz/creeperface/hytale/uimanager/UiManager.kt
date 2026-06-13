@file:OptIn(ExperimentalAtomicApi::class, ExperimentalTime::class)

package cz.creeperface.hytale.uimanager

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.packets.interface_.*
import com.hypixel.hytale.server.core.asset.common.CommonAssetModule
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.ui.builder.EventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.Universe
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import cz.creeperface.hytale.uimanager.UiManager.update
import cz.creeperface.hytale.uimanager.UiManager.updateAsync
import cz.creeperface.hytale.uimanager.UiManager.updatePage
import cz.creeperface.hytale.uimanager.UiManager.updatePageAsync
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
import cz.creeperface.hytale.uimanager.util.debug
import cz.creeperface.hytale.uimanager.util.getComponent
import cz.creeperface.hytale.uimanager.util.inDebug
import cz.creeperface.hytale.uimanager.util.player
import java.util.concurrent.*
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

class HudPageInstance(
    val pageId: String,
    var page: UiPage,
    var lastSentPage: UiPage,
    val pageData: PageData,
    val dynamicPageData: DynamicPageData?,
    var firstRender: Boolean = true,
    val scheduledCommands: MutableList<CustomUICommand>,
    var lastUpdate: Instant,
) {
    /** Set true once the instance is hidden/disconnected; an in-flight async build drops its result. */
    @Volatile
    var removed: Boolean = false

    /** Serializes async builds for this instance (latest-wins). */
    val asyncRunner = CoalescingBuildRunner()

    /** Guards [scheduledCommands] / [lastSentPage] / [page] hand-off between build threads and the tick drainer. */
    val publishLock = Any()
}

class PageInstance(
    val pageId: String,
    var page: UiPage,
    var lifeTime: CustomPageLifetime,
    var lastSentPage: UiPage,
    val pageData: PageData,
    eventBindings: MutableMap<Int, EventBinding>,
    val userData: Any
) {
    /** Reassigned on each update (build thread); read by client-event handling on another thread. */
    @Volatile
    var eventBindings: MutableMap<Int, EventBinding> = eventBindings

    /** Set true once the page is dismissed/disconnected; an in-flight async build drops its result. */
    @Volatile
    var removed: Boolean = false

    /** Serializes async page builds for this instance (latest-wins). */
    val asyncRunner = CoalescingBuildRunner()
}

data class UpdateOptions(
    val resendInputs: Boolean = true,
)

object UiManager {
    private val logger = HytaleLogger.forEnclosingClass()

    const val ASSET_PATH = "UI/Custom/Pages/UiManager/"
    const val PAGE_PATH = "Pages/UiManager/"

    private var closed = false

    private val pageIdRegex = "^[a-zA-Z0-9]+$".toRegex()

    private val pageCounter = AtomicInteger(0)

    private val pages = mutableMapOf<String, PageData>()
    private val dynamicPages = mutableMapOf<String, DynamicPageData>()

    // Concurrent so show*/update*/hide/disconnect can run from different world threads.
    // Callers must still invoke a given player's UI from that player's own world thread.
    private val openPages = ConcurrentHashMap<PlayerRef, PageInstance>()
    private val openHuds = ConcurrentHashMap<PlayerRef, MutableMap<String, HudPageInstance>>()

    private val updateLock = ReentrantReadWriteLock()
    private val scheduledUpdates = ConcurrentHashMap<PlayerRef, MutableSet<HudPageInstance>>()
    private val pendingHudRemovals = ConcurrentHashMap<PlayerRef, MutableSet<String>>()

    // CPU-bound build/diff work — small fixed daemon pool, not commonPool().
    private val defaultBuildExecutor: ExecutorService =
        Executors.newFixedThreadPool(
            (Runtime.getRuntime().availableProcessors() - 2).coerceIn(1, 4),
        ) { r -> Thread(r, "UiManager-build").apply { isDaemon = true } }

    /** Executor used by [updateAsync] when no per-call executor is supplied. Overridable. */
    @Volatile
    var uiBuildExecutor: Executor = defaultBuildExecutor

    internal fun shutdown() {
        defaultBuildExecutor.shutdown()
    }

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

        logger.debug {
            "Serialized: $pageId"
        }
        logger.debug {
            serializedPage
        }

        val fileName = "Page" + pageCounter.getAndIncrement() /*+ "_"*/ + pageId + ".ui"

        val assetName = ASSET_PATH + fileName

        logger.debug {
            "Adding asset $assetName - file name: $fileName"
        }
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

        val playerHuds = openHuds.computeIfAbsent(playerRef) { ConcurrentHashMap<String, HudPageInstance>() }

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
        require(playerHuds.putIfAbsent(pageId, pageInstance) == null) {
            "Page ID '$pageId' is already shown to player ${playerRef.username}"
        }

        logger.debug {
            "Showing dynamic HUD with ID '$pageId' to player ${playerRef.username}"
        }
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
            commandBuilder,
            initialShow = true
        )

        val playerHuds = openHuds.computeIfAbsent(playerRef) { ConcurrentHashMap<String, HudPageInstance>() }

        logger.inDebug {
            commandBuilder.commands.forEach { command ->
                it.log("HUD Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
            }
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
        require(playerHuds.putIfAbsent(pageId, pageInstance) == null) {
            "Page ID '$pageId' is already shown to player ${playerRef.username}"
        }

        scheduleHudUpdate(playerRef, pageInstance)
    }

    fun hideHud(pageId: String, playerRef: PlayerRef) {
        val instance = openHuds[playerRef]?.remove(pageId) ?: return
        instance.removed = true

        updateLock.read {
            pendingHudRemovals.computeIfAbsent(playerRef) { mutableSetOf() }.add(pageId)
            scheduledUpdates.computeIfAbsent(playerRef) { mutableSetOf() }
        }
    }

    /**
     * Runs [action] on the owning player's world thread: at the end of the current tick if the
     * caller is already on that world thread, otherwise on the next tick. Drops silently if the
     * world is no longer loaded. Lets thread-affine engine calls be invoked from any thread.
     */
    private fun executeOnPlayerWorld(playerRef: PlayerRef, action: () -> Unit) {
        val worldUuid = playerRef.worldUuid ?: return
        val world = Universe.get().getWorld(worldUuid) ?: return
        world.execute { action() }
    }

    /**
     * Opens an interactive page for the player. Safe to call from any thread: the actual open is
     * scheduled on the player's world thread, so this **returns before the page is opened** (end of
     * the current tick if already on that world thread, otherwise next tick). Argument validation
     * still happens synchronously on the caller.
     */
    fun showPage(
        playerRef: PlayerRef,
        pageId: String,
        userData: Any,
        lifetime: CustomPageLifetime = CustomPageLifetime.CanDismissOrCloseThroughInteraction,
        formData: List<Any?> = emptyList()
    ) {
        val pageData = pages[pageId]!!

        require(pageData.initialDataClass.isInstance(userData)) {
            "Context type ${userData::class} is not compatible with expected type ${pageData.initialDataClass}"
        }

        // openCustomPage touches the page manager + entity store, so run it on the owning player's
        // world thread. This makes showPage safe to call from any thread (end of the current tick
        // if already on that world thread, otherwise next tick).
        executeOnPlayerWorld(playerRef) {
            openInteractivePage(playerRef, pageId, pageData, userData, lifetime, formData)
        }
    }

    private fun openInteractivePage(
        playerRef: PlayerRef,
        pageId: String,
        pageData: PageData,
        userData: Any,
        lifetime: CustomPageLifetime,
        formData: List<Any?>,
    ) {
        val pageManager = playerRef.player.pageManager
        val ref = playerRef.reference ?: return

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
                    commandBuilder,
                    initialShow = true
                )

                logger.inDebug {
                    commandBuilder.commands.forEach { command ->
                        it.log("Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
                    }
                }

                logger.inDebug {
                    eventBindings.forEach { i, binding ->
                        it.log("Event binding: " + binding.type.name + " - ${binding.nodeId}")
                    }
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
                logger.debug {
                    "handleDataEvent: $rawData"
                }

                val json = JsonParser.parseString(rawData)

                if (json is JsonObject && json.has("FormIndex")) {
                    val response = try {
                        FormResponse.fromJson(json)
                    } catch (e: Exception) {
                        logger.atWarning().withCause(e).log("Failed to parse form response")
                        return
                    }

                    handleFormSubmit(playerRef, pageData, response)
                    return
                }

                val eventResponse = try {
                    EventResponse.fromJson(json)
                } catch (e: Exception) {
                    logger.atWarning().withCause(e).log("Failed to parse response")
                    return
                }

                logger.debug {
                    "response: $eventResponse"
                }

                logger.inDebug {
                    eventResponse.values.forEach { string, any ->
                        it.log("key: $string, value type: ${any?.javaClass?.simpleName}, value: $any")
                    }
                }

                handlePageEvent(playerRef, pageData, eventResponse)
            }

            override fun onDismiss(ref: Ref<EntityStore>, store: Store<EntityStore>) {
                val playerRef = ref.getComponent(PlayerRef.getComponentType()) ?: return

                openPages.remove(playerRef)?.let { it.removed = true }
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
            logger.atWarning().log("Form not found for index ${response.formIndex}")
            return
        }

        @Suppress("UNCHECKED_CAST")
        val castedForm = form as UiForm<Any>
        val submitHandler = castedForm.submitHandler

        if (submitHandler == null) {
            logger.atWarning().log("Submit handler not found for form ${response.formIndex}")
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
        formData: List<Any?> = emptyList(),
        options: UpdateOptions = UpdateOptions(),
    ) {
        val openPage = openPages[playerRef] ?: return

        if (openPage.pageId != pageId) {
            return
        }

        val newPage = openPage.pageData.factory(playerRef, userData)
        extractPageForms(newPage)

        val commandBuilder = UICommandBuilder()
        val eventBuilder = UIEventBuilder()

        UiDiffProcessor.generateUpdateCommands(openPage.lastSentPage, newPage, commandBuilder, options.resendInputs)
        val eventBindings = addEventBindings(newPage, eventBuilder)

        processPageForms(openPage.pageData, commandBuilder, eventBuilder, formData)

        logger.inDebug {
            commandBuilder.commands.forEach { command ->
                it.log("Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
            }
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

    /**
     * Off-thread sibling of [updatePage]: builds + diffs the open interactive page on [executor]
     * and sends the resulting `CustomPage` packet, instead of doing it on the caller (world) thread.
     * Builds for the same page instance are serialized and coalesced latest-wins. The returned
     * future completes after the packet is sent, or exceptionally if the build fails (page state is
     * left untouched on failure).
     *
     * The page factory MUST be safe to run off the world thread: it may read only the supplied
     * [userData] snapshot and immutable/registered data — never live ECS/world/entity state.
     *
     * A given open page must be updated through either [updatePage] or [updatePageAsync], not both
     * concurrently: async builds are serialized against each other, but not against the synchronous
     * path, which would race on [PageInstance.lastSentPage] / [PageInstance.eventBindings].
     */
    fun updatePageAsync(
        playerRef: PlayerRef,
        pageId: String,
        userData: Any = Unit,
        clear: Boolean = false,
        formData: List<Any?> = emptyList(),
        options: UpdateOptions = UpdateOptions(),
        executor: Executor = uiBuildExecutor,
    ): CompletableFuture<Void> {
        // Resolve the instance on the caller thread (openPages is not thread-safe).
        val openPage = openPages[playerRef] ?: return CompletableFuture.completedFuture<Void>(null)
        if (openPage.pageId != pageId) return CompletableFuture.completedFuture<Void>(null)

        return openPage.asyncRunner.submit(executor) {
            buildAndSendPageAsync(playerRef, openPage, pageId, userData, clear, formData, options)
        }
    }

    private fun buildAndSendPageAsync(
        playerRef: PlayerRef,
        openPage: PageInstance,
        pageId: String,
        userData: Any,
        clear: Boolean,
        formData: List<Any?>,
        options: UpdateOptions,
    ) {
        try {
            val newPage = openPage.pageData.factory(playerRef, userData)
            extractPageForms(newPage)

            val commandBuilder = UICommandBuilder()
            val eventBuilder = UIEventBuilder()

            UiDiffProcessor.generateUpdateCommands(openPage.lastSentPage, newPage, commandBuilder, options.resendInputs)
            val eventBindings = addEventBindings(newPage, eventBuilder)

            processPageForms(openPage.pageData, commandBuilder, eventBuilder, formData)

            // Skip if the page was dismissed/disconnected mid-build.
            if (openPage.removed) return

            playerRef.packetHandler.writeNoCache(
                CustomPage(
                    pageId,
                    false,
                    clear,
                    openPage.lifeTime,
                    commandBuilder.commands,
                    eventBuilder.events
                )
            )

            openPage.eventBindings = eventBindings.toMutableMap()
            openPage.lastSentPage = newPage
        } catch (e: Throwable) {
            logger.atWarning().withCause(e).log("Async page build failed for page '$pageId'")
            throw e
        }
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

        logger.inDebug {
            commandBuilder.commands.forEach { command ->
                it.log("HUD Command: ${command.type} - ${command.selector}, ${command.data}, ${command.text}")
            }
        }

        synchronized(pageInstance.publishLock) {
            pageInstance.lastSentPage = sendPage
            pageInstance.scheduledCommands.clear()
            pageInstance.scheduledCommands.addAll(commandBuilder.commands)
        }

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
                synchronized(instance.publishLock) {
                    val entry = instance.pageId to instance.scheduledCommands.toList()
                    instance.scheduledCommands.clear()
                    entry
                }
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

            if (commands.isNotEmpty()) {
                playerRef.packetHandler.writeNoCache(
                    CustomHud(
                        "UiManager",
                        100,
                        false,
                        commands.toTypedArray()
                    )
                )
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

    /**
     * Build + diff a dynamic HUD off the caller (world) thread and enqueue the resulting
     * commands for the next tick. Builds for the same instance are serialized and coalesced
     * latest-wins. The returned future completes after the result is published (not after the
     * packet is sent), or exceptionally if the build fails (state is left untouched on failure).
     *
     * The page factory MUST be safe to run off the world thread: it may read only the supplied
     * [context] snapshot and immutable/registered data — never live ECS/world/entity state.
     *
     * A given HUD instance must be updated through either [update] or [updateAsync], not both
     * concurrently: async builds are serialized against each other, but not against the
     * synchronous path, which would race on [HudPageInstance.lastSentPage].
     */
    fun updateAsync(
        playerRef: PlayerRef,
        pageId: String,
        context: Any,
        executor: Executor = uiBuildExecutor,
    ): CompletableFuture<Void> {
        // Resolve the instance on the caller thread (openHuds is not thread-safe).
        val instance = openHuds[playerRef]?.get(pageId)
            ?: return CompletableFuture.completedFuture<Void>(null)
        requireNotNull(instance.dynamicPageData) {
            "Trying to update page ID '$pageId' which is not dynamic"
        }

        return instance.asyncRunner.submit(executor) {
            buildAndPublishAsync(playerRef, instance, context)
        }
    }

    private fun buildAndPublishAsync(playerRef: PlayerRef, instance: HudPageInstance, context: Any) {
        val dynamicPageData = instance.dynamicPageData!!
        try {
            val page = dynamicPageData.factory(playerRef, context)
            page.resetDirty()
            val sendPage = page.clone()

            val commandBuilder = UICommandBuilder()
            UiDiffProcessor.generateUpdateCommands(instance.lastSentPage, sendPage, commandBuilder)

            // Publish only on success; skip if the instance was hidden/disconnected mid-build.
            if (instance.removed) return
            synchronized(instance.publishLock) {
                instance.page = page
                instance.lastSentPage = sendPage
                instance.scheduledCommands.clear()
                instance.scheduledCommands.addAll(commandBuilder.commands)
            }
            scheduleHudUpdate(playerRef, instance)
        } catch (e: Throwable) {
            logger.atWarning().withCause(e).log("Async UI build failed for page '${instance.pageId}'")
            throw e
        }
    }

    internal fun onPlayerDisconnect(playerRef: PlayerRef) {
        openHuds.remove(playerRef)?.values?.forEach { it.removed = true }
        openPages.remove(playerRef)?.let { it.removed = true }
        pendingHudRemovals.remove(playerRef)
    }

    private fun extractPageForms(page: UiPage): List<UiForm<*>> {
        val forms = mutableListOf<UiForm<*>>()

        fun processNode(node: UiNode): List<UiNode> {
            val nodes: MutableList<UiNode> = mutableListOf()

            when (node) {
                is UiForm<*> -> {
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
}
