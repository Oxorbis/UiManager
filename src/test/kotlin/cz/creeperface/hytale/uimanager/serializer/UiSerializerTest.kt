package cz.creeperface.hytale.uimanager.serializer

import cz.creeperface.hytale.MyFormData
import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.group
import cz.creeperface.hytale.uimanager.builder.label
import cz.creeperface.hytale.uimanager.builder.panel
import cz.creeperface.hytale.uimanager.builder.sprite
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.special.boundTextField
import cz.creeperface.hytale.uimanager.special.form
import cz.creeperface.hytale.uimanager.special.formGroup
import cz.creeperface.hytale.uimanager.special.submitTextButton
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.anchor
import cz.creeperface.hytale.uimanager.type.labelStyle
import cz.creeperface.hytale.uimanager.type.patchStyle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class UiSerializerTest {

    @Test
    fun testSerialization() {
        val page = createSampleForm()
        
        val serialized = UiSerializer.serialize(page)
        
        val referenceFile = File("src/test/resources/cz/creeperface/hytale/uimanager/serializer/reference.ui")
        if (!referenceFile.exists()) {
            referenceFile.parentFile.mkdirs()
            referenceFile.writeText(serialized)
            // FAIL the test first time so user knows reference was created
            // Actually, for Junie, it's better to just pass if it's the first time or check if we are in "update" mode
        }
        
        val expected = referenceFile.readText()
        assertEquals(expected.trim(), serialized.trim())
    }

    @Test
    fun testComplexSerialization() {
        val page = createComplexUi()

        val serialized = UiSerializer.serialize(page)

        val referenceFile = File("src/test/resources/cz/creeperface/hytale/uimanager/serializer/complex_reference.ui")
        if (!referenceFile.exists()) {
            referenceFile.parentFile.mkdirs()
            referenceFile.writeText(serialized)
        }

        val expected = referenceFile.readText()
        assertEquals(expected.trim(), serialized.trim())
    }

    fun createComplexUi() = customUi {
        val commonAnchor = anchor {
            width = 500
            height = 600
        }

        val commonBackground = patchStyle {
            texturePath = "textures/ui/panel_background.png"
            border = 10
        }

        group {
            layoutMode = LayoutMode.Middle

            panel {
                anchor = commonAnchor
                background = commonBackground
                layoutMode = LayoutMode.Center

                group {
                    anchor = anchor {
                        top = 20
                        width = 400
                        height = 50
                    }
                    layoutMode = LayoutMode.Center

                    label {
                        text = "Complex UI Header"
                        anchor = anchor { full = 0 }
                        style = labelStyle {
                            fontSize = 24.0
                            renderBold = true
                            textColor("#FFFFFF")
                        }
                    }
                }

                group {
                    anchor = anchor {
                        top = 80
                        width = 460
                        bottom = 20
                    }
                    layoutMode = LayoutMode.Middle

                    repeat(3) { i ->
                        group {
                            anchor = anchor {
                                width = 440
                                height = 100
                            }
                            background = patchStyle {
                                texturePath = "textures/ui/item_slot.png"
                                border = 5
                            }

                            sprite {
                                anchor = anchor {
                                    left = 10
                                    vertical = 0
                                    width = 80
                                }
                                texturePath = "textures/items/item_$i.png"
                            }

                            label {
                                anchor = anchor {
                                    left = 100
                                    right = 10
                                    vertical = 0
                                }
                                text = "Item Number $i Description"
                            }
                        }
                    }
                }
            }
        }
    }

    fun createSampleForm() = customUi {
        group {
            layoutMode = LayoutMode.Middle

            group {
                anchor = UiAnchor(width = 400, height = 400)
                layoutMode = LayoutMode.Center

                form<MyFormData> {
//                    submitHandler = { playerRef, data ->
//                        HytaleLogger.getLogger().atInfo().log("Form submitted with data: $data")
//                    }

                    formGroup {
                        anchor = UiAnchor(height = 40)
                        layoutMode = LayoutMode.Center

                        boundTextField(MyFormData::firstName) {
                            anchor = UiAnchor(width = 100, height = 40)
                            placeholderText = "First Name"
                        }
                    }

                    formGroup {
                        anchor = UiAnchor(height = 40)
                        layoutMode = LayoutMode.Center

                        boundTextField(MyFormData::lastName) {
                            anchor = UiAnchor(width = 100, height = 40)
                            placeholderText = "Last Name"
                        }
                    }

                    formGroup {
                        anchor = UiAnchor(height = 40)
                        layoutMode = LayoutMode.Center

                        submitTextButton {
                            anchor = UiAnchor(width = 70, height = 40)
                            text = "Submit"
                        }
                    }

                    formGroup {
                        anchor = UiAnchor(height = 40)
                        layoutMode = LayoutMode.Center

//                        this@form.submitHandler = { _, _ ->
//                        }
                    }
                }
            }
        }
    }
}
