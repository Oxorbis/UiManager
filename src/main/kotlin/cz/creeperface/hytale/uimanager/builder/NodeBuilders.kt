package cz.creeperface.hytale.uimanager.builder

import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.IdGenerator
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.node.UiActionButton
import cz.creeperface.hytale.uimanager.node.UiAssetImage
import cz.creeperface.hytale.uimanager.node.UiBackButton
import cz.creeperface.hytale.uimanager.node.UiBlockSelector
import cz.creeperface.hytale.uimanager.node.UiButton
import cz.creeperface.hytale.uimanager.node.UiCharacterPreviewComponent
import cz.creeperface.hytale.uimanager.node.UiCheckBox
import cz.creeperface.hytale.uimanager.node.UiCheckBoxContainer
import cz.creeperface.hytale.uimanager.node.UiCircularProgressBar
import cz.creeperface.hytale.uimanager.node.UiCodeEditor
import cz.creeperface.hytale.uimanager.node.UiColorOptionGrid
import cz.creeperface.hytale.uimanager.node.UiColorPicker
import cz.creeperface.hytale.uimanager.node.UiColorPickerDropdownBox
import cz.creeperface.hytale.uimanager.node.UiCompactTextField
import cz.creeperface.hytale.uimanager.node.UiDropdownBox
import cz.creeperface.hytale.uimanager.node.UiDropdownEntry
import cz.creeperface.hytale.uimanager.node.UiDynamicPane
import cz.creeperface.hytale.uimanager.node.UiDynamicPaneContainer
import cz.creeperface.hytale.uimanager.node.UiFloatSlider
import cz.creeperface.hytale.uimanager.node.UiFloatSliderNumberField
import cz.creeperface.hytale.uimanager.node.UiGroup
import cz.creeperface.hytale.uimanager.node.UiHotkeyLabel
import cz.creeperface.hytale.uimanager.node.UiItemGrid
import cz.creeperface.hytale.uimanager.node.UiItemIcon
import cz.creeperface.hytale.uimanager.node.UiItemPreviewComponent
import cz.creeperface.hytale.uimanager.node.UiItemSlot
import cz.creeperface.hytale.uimanager.node.UiItemSlotButton
import cz.creeperface.hytale.uimanager.node.UiLabel
import cz.creeperface.hytale.uimanager.node.UiLabeledCheckBox
import cz.creeperface.hytale.uimanager.node.UiMenuItem
import cz.creeperface.hytale.uimanager.node.UiMultilineTextField
import cz.creeperface.hytale.uimanager.node.UiNumberField
import cz.creeperface.hytale.uimanager.node.UiPanel
import cz.creeperface.hytale.uimanager.node.UiProgressBar
import cz.creeperface.hytale.uimanager.node.UiReorderableList
import cz.creeperface.hytale.uimanager.node.UiReorderableListGrip
import cz.creeperface.hytale.uimanager.node.UiSceneBlur
import cz.creeperface.hytale.uimanager.node.UiSlider
import cz.creeperface.hytale.uimanager.node.UiSliderNumberField
import cz.creeperface.hytale.uimanager.node.UiSprite
import cz.creeperface.hytale.uimanager.node.UiTabButton
import cz.creeperface.hytale.uimanager.node.UiTabNavigation
import cz.creeperface.hytale.uimanager.node.UiTextButton
import cz.creeperface.hytale.uimanager.node.UiTextField
import cz.creeperface.hytale.uimanager.node.UiTimerLabel
import cz.creeperface.hytale.uimanager.node.UiToggleButton
import kotlin.Unit

public fun ChildNodeBuilder.toggleButton(`init`: UiToggleButton.() -> Unit): UiToggleButton {
  val node = UiToggleButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ToggleButton")
  node.init()
  this.addNode(node)
  return node
}

public fun toggleButton(`init`: UiToggleButton.() -> Unit): UiToggleButton {
  val node = UiToggleButton()
  node.id = IdGenerator.getNext("ToggleButton")
  node.init()
  return node
}

public fun ChildNodeBuilder.label(`init`: UiLabel.() -> Unit): UiLabel {
  val node = UiLabel()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "Label")
  node.init()
  this.addNode(node)
  return node
}

public fun label(`init`: UiLabel.() -> Unit): UiLabel {
  val node = UiLabel()
  node.id = IdGenerator.getNext("Label")
  node.init()
  return node
}

public fun ChildNodeBuilder.colorOptionGrid(`init`: UiColorOptionGrid.() -> Unit): UiColorOptionGrid {
  val node = UiColorOptionGrid()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ColorOptionGrid")
  node.init()
  this.addNode(node)
  return node
}

public fun colorOptionGrid(`init`: UiColorOptionGrid.() -> Unit): UiColorOptionGrid {
  val node = UiColorOptionGrid()
  node.id = IdGenerator.getNext("ColorOptionGrid")
  node.init()
  return node
}

public fun ChildNodeBuilder.compactTextField(`init`: UiCompactTextField.() -> Unit): UiCompactTextField {
  val node = UiCompactTextField()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "CompactTextField")
  node.init()
  this.addNode(node)
  return node
}

public fun compactTextField(`init`: UiCompactTextField.() -> Unit): UiCompactTextField {
  val node = UiCompactTextField()
  node.id = IdGenerator.getNext("CompactTextField")
  node.init()
  return node
}

public fun ChildNodeBuilder.group(`init`: UiGroup.() -> Unit): UiGroup {
  val node = UiGroup()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "Group")
  node.init()
  this.addNode(node)
  return node
}

public fun group(`init`: UiGroup.() -> Unit): UiGroup {
  val node = UiGroup()
  node.id = IdGenerator.getNext("Group")
  node.init()
  return node
}

public fun ChildNodeBuilder.labeledCheckBox(`init`: UiLabeledCheckBox.() -> Unit): UiLabeledCheckBox {
  val node = UiLabeledCheckBox()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "LabeledCheckBox")
  node.init()
  this.addNode(node)
  return node
}

public fun labeledCheckBox(`init`: UiLabeledCheckBox.() -> Unit): UiLabeledCheckBox {
  val node = UiLabeledCheckBox()
  node.id = IdGenerator.getNext("LabeledCheckBox")
  node.init()
  return node
}

public fun ChildNodeBuilder.slider(`init`: UiSlider.() -> Unit): UiSlider {
  val node = UiSlider()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "Slider")
  node.init()
  this.addNode(node)
  return node
}

public fun slider(`init`: UiSlider.() -> Unit): UiSlider {
  val node = UiSlider()
  node.id = IdGenerator.getNext("Slider")
  node.init()
  return node
}

public fun ChildNodeBuilder.sprite(`init`: UiSprite.() -> Unit): UiSprite {
  val node = UiSprite()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "Sprite")
  node.init()
  this.addNode(node)
  return node
}

public fun sprite(`init`: UiSprite.() -> Unit): UiSprite {
  val node = UiSprite()
  node.id = IdGenerator.getNext("Sprite")
  node.init()
  return node
}

public fun ChildNodeBuilder.sceneBlur(`init`: UiSceneBlur.() -> Unit): UiSceneBlur {
  val node = UiSceneBlur()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "SceneBlur")
  node.init()
  this.addNode(node)
  return node
}

public fun sceneBlur(`init`: UiSceneBlur.() -> Unit): UiSceneBlur {
  val node = UiSceneBlur()
  node.id = IdGenerator.getNext("SceneBlur")
  node.init()
  return node
}

public fun ChildNodeBuilder.floatSliderNumberField(`init`: UiFloatSliderNumberField.() -> Unit): UiFloatSliderNumberField {
  val node = UiFloatSliderNumberField()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "FloatSliderNumberField")
  node.init()
  this.addNode(node)
  return node
}

public fun floatSliderNumberField(`init`: UiFloatSliderNumberField.() -> Unit): UiFloatSliderNumberField {
  val node = UiFloatSliderNumberField()
  node.id = IdGenerator.getNext("FloatSliderNumberField")
  node.init()
  return node
}

public fun ChildNodeBuilder.codeEditor(`init`: UiCodeEditor.() -> Unit): UiCodeEditor {
  val node = UiCodeEditor()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "CodeEditor")
  node.init()
  this.addNode(node)
  return node
}

public fun codeEditor(`init`: UiCodeEditor.() -> Unit): UiCodeEditor {
  val node = UiCodeEditor()
  node.id = IdGenerator.getNext("CodeEditor")
  node.init()
  return node
}

public fun ChildNodeBuilder.menuItem(`init`: UiMenuItem.() -> Unit): UiMenuItem {
  val node = UiMenuItem()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "MenuItem")
  node.init()
  this.addNode(node)
  return node
}

public fun menuItem(`init`: UiMenuItem.() -> Unit): UiMenuItem {
  val node = UiMenuItem()
  node.id = IdGenerator.getNext("MenuItem")
  node.init()
  return node
}

public fun ChildNodeBuilder.multilineTextField(`init`: UiMultilineTextField.() -> Unit): UiMultilineTextField {
  val node = UiMultilineTextField()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "MultilineTextField")
  node.init()
  this.addNode(node)
  return node
}

public fun multilineTextField(`init`: UiMultilineTextField.() -> Unit): UiMultilineTextField {
  val node = UiMultilineTextField()
  node.id = IdGenerator.getNext("MultilineTextField")
  node.init()
  return node
}

public fun ChildNodeBuilder.progressBar(`init`: UiProgressBar.() -> Unit): UiProgressBar {
  val node = UiProgressBar()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ProgressBar")
  node.init()
  this.addNode(node)
  return node
}

public fun progressBar(`init`: UiProgressBar.() -> Unit): UiProgressBar {
  val node = UiProgressBar()
  node.id = IdGenerator.getNext("ProgressBar")
  node.init()
  return node
}

public fun ChildNodeBuilder.blockSelector(`init`: UiBlockSelector.() -> Unit): UiBlockSelector {
  val node = UiBlockSelector()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "BlockSelector")
  node.init()
  this.addNode(node)
  return node
}

public fun blockSelector(`init`: UiBlockSelector.() -> Unit): UiBlockSelector {
  val node = UiBlockSelector()
  node.id = IdGenerator.getNext("BlockSelector")
  node.init()
  return node
}

public fun ChildNodeBuilder.button(`init`: UiButton.() -> Unit): UiButton {
  val node = UiButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "Button")
  node.init()
  this.addNode(node)
  return node
}

public fun button(`init`: UiButton.() -> Unit): UiButton {
  val node = UiButton()
  node.id = IdGenerator.getNext("Button")
  node.init()
  return node
}

public fun ChildNodeBuilder.actionButton(`init`: UiActionButton.() -> Unit): UiActionButton {
  val node = UiActionButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ActionButton")
  node.init()
  this.addNode(node)
  return node
}

public fun actionButton(`init`: UiActionButton.() -> Unit): UiActionButton {
  val node = UiActionButton()
  node.id = IdGenerator.getNext("ActionButton")
  node.init()
  return node
}

public fun ChildNodeBuilder.colorPickerDropdownBox(`init`: UiColorPickerDropdownBox.() -> Unit): UiColorPickerDropdownBox {
  val node = UiColorPickerDropdownBox()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ColorPickerDropdownBox")
  node.init()
  this.addNode(node)
  return node
}

public fun colorPickerDropdownBox(`init`: UiColorPickerDropdownBox.() -> Unit): UiColorPickerDropdownBox {
  val node = UiColorPickerDropdownBox()
  node.id = IdGenerator.getNext("ColorPickerDropdownBox")
  node.init()
  return node
}

public fun ChildNodeBuilder.reorderableList(`init`: UiReorderableList.() -> Unit): UiReorderableList {
  val node = UiReorderableList()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ReorderableList")
  node.init()
  this.addNode(node)
  return node
}

public fun reorderableList(`init`: UiReorderableList.() -> Unit): UiReorderableList {
  val node = UiReorderableList()
  node.id = IdGenerator.getNext("ReorderableList")
  node.init()
  return node
}

public fun ChildNodeBuilder.tabButton(`init`: UiTabButton.() -> Unit): UiTabButton {
  val node = UiTabButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "TabButton")
  node.init()
  this.addNode(node)
  return node
}

public fun tabButton(`init`: UiTabButton.() -> Unit): UiTabButton {
  val node = UiTabButton()
  node.id = IdGenerator.getNext("TabButton")
  node.init()
  return node
}

public fun ChildNodeBuilder.characterPreviewComponent(`init`: UiCharacterPreviewComponent.() -> Unit): UiCharacterPreviewComponent {
  val node = UiCharacterPreviewComponent()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "CharacterPreviewComponent")
  node.init()
  this.addNode(node)
  return node
}

public fun characterPreviewComponent(`init`: UiCharacterPreviewComponent.() -> Unit): UiCharacterPreviewComponent {
  val node = UiCharacterPreviewComponent()
  node.id = IdGenerator.getNext("CharacterPreviewComponent")
  node.init()
  return node
}

public fun ChildNodeBuilder.itemSlotButton(`init`: UiItemSlotButton.() -> Unit): UiItemSlotButton {
  val node = UiItemSlotButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ItemSlotButton")
  node.init()
  this.addNode(node)
  return node
}

public fun itemSlotButton(`init`: UiItemSlotButton.() -> Unit): UiItemSlotButton {
  val node = UiItemSlotButton()
  node.id = IdGenerator.getNext("ItemSlotButton")
  node.init()
  return node
}

public fun ChildNodeBuilder.checkBox(`init`: UiCheckBox.() -> Unit): UiCheckBox {
  val node = UiCheckBox()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "CheckBox")
  node.init()
  this.addNode(node)
  return node
}

public fun checkBox(`init`: UiCheckBox.() -> Unit): UiCheckBox {
  val node = UiCheckBox()
  node.id = IdGenerator.getNext("CheckBox")
  node.init()
  return node
}

public fun ChildNodeBuilder.textField(`init`: UiTextField.() -> Unit): UiTextField {
  val node = UiTextField()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "TextField")
  node.init()
  this.addNode(node)
  return node
}

public fun textField(`init`: UiTextField.() -> Unit): UiTextField {
  val node = UiTextField()
  node.id = IdGenerator.getNext("TextField")
  node.init()
  return node
}

public fun ChildNodeBuilder.itemPreviewComponent(`init`: UiItemPreviewComponent.() -> Unit): UiItemPreviewComponent {
  val node = UiItemPreviewComponent()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ItemPreviewComponent")
  node.init()
  this.addNode(node)
  return node
}

public fun itemPreviewComponent(`init`: UiItemPreviewComponent.() -> Unit): UiItemPreviewComponent {
  val node = UiItemPreviewComponent()
  node.id = IdGenerator.getNext("ItemPreviewComponent")
  node.init()
  return node
}

public fun ChildNodeBuilder.panel(`init`: UiPanel.() -> Unit): UiPanel {
  val node = UiPanel()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "Panel")
  node.init()
  this.addNode(node)
  return node
}

public fun panel(`init`: UiPanel.() -> Unit): UiPanel {
  val node = UiPanel()
  node.id = IdGenerator.getNext("Panel")
  node.init()
  return node
}

public fun ChildNodeBuilder.reorderableListGrip(`init`: UiReorderableListGrip.() -> Unit): UiReorderableListGrip {
  val node = UiReorderableListGrip()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ReorderableListGrip")
  node.init()
  this.addNode(node)
  return node
}

public fun reorderableListGrip(`init`: UiReorderableListGrip.() -> Unit): UiReorderableListGrip {
  val node = UiReorderableListGrip()
  node.id = IdGenerator.getNext("ReorderableListGrip")
  node.init()
  return node
}

public fun ChildNodeBuilder.dropdownEntry(`init`: UiDropdownEntry.() -> Unit): UiDropdownEntry {
  val node = UiDropdownEntry()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "DropdownEntry")
  node.init()
  this.addNode(node)
  return node
}

public fun dropdownEntry(`init`: UiDropdownEntry.() -> Unit): UiDropdownEntry {
  val node = UiDropdownEntry()
  node.id = IdGenerator.getNext("DropdownEntry")
  node.init()
  return node
}

public fun ChildNodeBuilder.backButton(`init`: UiBackButton.() -> Unit): UiBackButton {
  val node = UiBackButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "BackButton")
  node.init()
  this.addNode(node)
  return node
}

public fun backButton(`init`: UiBackButton.() -> Unit): UiBackButton {
  val node = UiBackButton()
  node.id = IdGenerator.getNext("BackButton")
  node.init()
  return node
}

public fun ChildNodeBuilder.itemSlot(`init`: UiItemSlot.() -> Unit): UiItemSlot {
  val node = UiItemSlot()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ItemSlot")
  node.init()
  this.addNode(node)
  return node
}

public fun itemSlot(`init`: UiItemSlot.() -> Unit): UiItemSlot {
  val node = UiItemSlot()
  node.id = IdGenerator.getNext("ItemSlot")
  node.init()
  return node
}

public fun ChildNodeBuilder.tabNavigation(`init`: UiTabNavigation.() -> Unit): UiTabNavigation {
  val node = UiTabNavigation()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "TabNavigation")
  node.init()
  this.addNode(node)
  return node
}

public fun tabNavigation(`init`: UiTabNavigation.() -> Unit): UiTabNavigation {
  val node = UiTabNavigation()
  node.id = IdGenerator.getNext("TabNavigation")
  node.init()
  return node
}

public fun ChildNodeBuilder.colorPicker(`init`: UiColorPicker.() -> Unit): UiColorPicker {
  val node = UiColorPicker()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ColorPicker")
  node.init()
  this.addNode(node)
  return node
}

public fun colorPicker(`init`: UiColorPicker.() -> Unit): UiColorPicker {
  val node = UiColorPicker()
  node.id = IdGenerator.getNext("ColorPicker")
  node.init()
  return node
}

public fun ChildNodeBuilder.hotkeyLabel(`init`: UiHotkeyLabel.() -> Unit): UiHotkeyLabel {
  val node = UiHotkeyLabel()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "HotkeyLabel")
  node.init()
  this.addNode(node)
  return node
}

public fun hotkeyLabel(`init`: UiHotkeyLabel.() -> Unit): UiHotkeyLabel {
  val node = UiHotkeyLabel()
  node.id = IdGenerator.getNext("HotkeyLabel")
  node.init()
  return node
}

public fun ChildNodeBuilder.numberField(`init`: UiNumberField.() -> Unit): UiNumberField {
  val node = UiNumberField()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "NumberField")
  node.init()
  this.addNode(node)
  return node
}

public fun numberField(`init`: UiNumberField.() -> Unit): UiNumberField {
  val node = UiNumberField()
  node.id = IdGenerator.getNext("NumberField")
  node.init()
  return node
}

public fun ChildNodeBuilder.dynamicPane(`init`: UiDynamicPane.() -> Unit): UiDynamicPane {
  val node = UiDynamicPane()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "DynamicPane")
  node.init()
  this.addNode(node)
  return node
}

public fun dynamicPane(`init`: UiDynamicPane.() -> Unit): UiDynamicPane {
  val node = UiDynamicPane()
  node.id = IdGenerator.getNext("DynamicPane")
  node.init()
  return node
}

public fun ChildNodeBuilder.sliderNumberField(`init`: UiSliderNumberField.() -> Unit): UiSliderNumberField {
  val node = UiSliderNumberField()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "SliderNumberField")
  node.init()
  this.addNode(node)
  return node
}

public fun sliderNumberField(`init`: UiSliderNumberField.() -> Unit): UiSliderNumberField {
  val node = UiSliderNumberField()
  node.id = IdGenerator.getNext("SliderNumberField")
  node.init()
  return node
}

public fun ChildNodeBuilder.dynamicPaneContainer(`init`: UiDynamicPaneContainer.() -> Unit): UiDynamicPaneContainer {
  val node = UiDynamicPaneContainer()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "DynamicPaneContainer")
  node.init()
  this.addNode(node)
  return node
}

public fun dynamicPaneContainer(`init`: UiDynamicPaneContainer.() -> Unit): UiDynamicPaneContainer {
  val node = UiDynamicPaneContainer()
  node.id = IdGenerator.getNext("DynamicPaneContainer")
  node.init()
  return node
}

public fun ChildNodeBuilder.floatSlider(`init`: UiFloatSlider.() -> Unit): UiFloatSlider {
  val node = UiFloatSlider()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "FloatSlider")
  node.init()
  this.addNode(node)
  return node
}

public fun floatSlider(`init`: UiFloatSlider.() -> Unit): UiFloatSlider {
  val node = UiFloatSlider()
  node.id = IdGenerator.getNext("FloatSlider")
  node.init()
  return node
}

public fun ChildNodeBuilder.dropdownBox(`init`: UiDropdownBox.() -> Unit): UiDropdownBox {
  val node = UiDropdownBox()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "DropdownBox")
  node.init()
  this.addNode(node)
  return node
}

public fun dropdownBox(`init`: UiDropdownBox.() -> Unit): UiDropdownBox {
  val node = UiDropdownBox()
  node.id = IdGenerator.getNext("DropdownBox")
  node.init()
  return node
}

public fun ChildNodeBuilder.timerLabel(`init`: UiTimerLabel.() -> Unit): UiTimerLabel {
  val node = UiTimerLabel()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "TimerLabel")
  node.init()
  this.addNode(node)
  return node
}

public fun timerLabel(`init`: UiTimerLabel.() -> Unit): UiTimerLabel {
  val node = UiTimerLabel()
  node.id = IdGenerator.getNext("TimerLabel")
  node.init()
  return node
}

public fun ChildNodeBuilder.circularProgressBar(`init`: UiCircularProgressBar.() -> Unit): UiCircularProgressBar {
  val node = UiCircularProgressBar()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "CircularProgressBar")
  node.init()
  this.addNode(node)
  return node
}

public fun circularProgressBar(`init`: UiCircularProgressBar.() -> Unit): UiCircularProgressBar {
  val node = UiCircularProgressBar()
  node.id = IdGenerator.getNext("CircularProgressBar")
  node.init()
  return node
}

public fun ChildNodeBuilder.checkBoxContainer(`init`: UiCheckBoxContainer.() -> Unit): UiCheckBoxContainer {
  val node = UiCheckBoxContainer()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "CheckBoxContainer")
  node.init()
  this.addNode(node)
  return node
}

public fun checkBoxContainer(`init`: UiCheckBoxContainer.() -> Unit): UiCheckBoxContainer {
  val node = UiCheckBoxContainer()
  node.id = IdGenerator.getNext("CheckBoxContainer")
  node.init()
  return node
}

public fun ChildNodeBuilder.itemIcon(`init`: UiItemIcon.() -> Unit): UiItemIcon {
  val node = UiItemIcon()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ItemIcon")
  node.init()
  this.addNode(node)
  return node
}

public fun itemIcon(`init`: UiItemIcon.() -> Unit): UiItemIcon {
  val node = UiItemIcon()
  node.id = IdGenerator.getNext("ItemIcon")
  node.init()
  return node
}

public fun ChildNodeBuilder.textButton(`init`: UiTextButton.() -> Unit): UiTextButton {
  val node = UiTextButton()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "TextButton")
  node.init()
  this.addNode(node)
  return node
}

public fun textButton(`init`: UiTextButton.() -> Unit): UiTextButton {
  val node = UiTextButton()
  node.id = IdGenerator.getNext("TextButton")
  node.init()
  return node
}

public fun ChildNodeBuilder.assetImage(`init`: UiAssetImage.() -> Unit): UiAssetImage {
  val node = UiAssetImage()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "AssetImage")
  node.init()
  this.addNode(node)
  return node
}

public fun assetImage(`init`: UiAssetImage.() -> Unit): UiAssetImage {
  val node = UiAssetImage()
  node.id = IdGenerator.getNext("AssetImage")
  node.init()
  return node
}

public fun ChildNodeBuilder.itemGrid(`init`: UiItemGrid.() -> Unit): UiItemGrid {
  val node = UiItemGrid()
  val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
  node.id = IdGenerator.getNext(prefix + "ItemGrid")
  node.init()
  this.addNode(node)
  return node
}

public fun itemGrid(`init`: UiItemGrid.() -> Unit): UiItemGrid {
  val node = UiItemGrid()
  node.id = IdGenerator.getNext("ItemGrid")
  node.init()
  return node
}
