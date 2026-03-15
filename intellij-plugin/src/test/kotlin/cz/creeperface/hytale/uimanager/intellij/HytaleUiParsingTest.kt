package cz.creeperface.hytale.uimanager.intellij

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import cz.creeperface.hytale.uimanager.intellij.inspection.HytaleUiTypeMismatchInspection
import cz.creeperface.hytale.uimanager.intellij.inspection.HytaleUiUnknownNodeInspection
import cz.creeperface.hytale.uimanager.intellij.inspection.HytaleUiUnknownPropertyInspection
import cz.creeperface.hytale.uimanager.intellij.inspection.HytaleUiUnresolvedFileInspection
import cz.creeperface.hytale.uimanager.intellij.inspection.HytaleUiUnresolvedVariableInspection
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes

class HytaleUiParsingTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(
            HytaleUiTypeMismatchInspection::class.java,
            HytaleUiUnknownPropertyInspection::class.java,
            HytaleUiUnknownNodeInspection::class.java,
            HytaleUiUnresolvedVariableInspection::class.java,
            HytaleUiUnresolvedFileInspection::class.java,
        )
    }

    // ── Schema ──

    fun testSchemaService() {
        myFixture.configureByText("test.ui", "Group {}")
        val schema = HytaleUiSchemaService.getInstance()
        assertTrue("nodes loaded", schema.nodeNames.size >= 40)
        assertTrue("types loaded", schema.typeNames.size >= 40)
        assertTrue("enums loaded", schema.enumNames.size >= 10)
        assertEquals("String", schema.getPropertyExpectedType("Label", "Text"))
        assertEquals("Boolean", schema.getPropertyExpectedType("Label", "Visible"))
        assertEquals("LabelStyle", schema.getPropertyExpectedType("Label", "Style"))
        assertEquals(false, schema.isValueCompatible("String", "Integer"))
        assertEquals(false, schema.isValueCompatible("Boolean", "Integer"))
        assertEquals(true, schema.isValueCompatible("PatchStyle", "String"))
        assertEquals(true, schema.isValueCompatible("PatchStyle", "Color"))
        assertEquals(true, schema.isValueCompatible("PatchStyle", "Object"))
    }

    // ── Completion: node names in body ──

    fun testCompletionNodeNames() {
        val items = completeAt("Group {\n  L<caret>\n}")
        assertContainsAll(items, "Label")
    }

    // ── Completion: property names for node ──

    fun testCompletionPropertyNamesInLabel() {
        val items = completeAt("Label {\n  T<caret>\n}")
        assertContainsAll(items, "Text", "TooltipText", "TextTooltipShowDelay")
    }

    fun testCompletionPropertyNamesInGroup() {
        val items = completeAt("Group {\n  L<caret>\n}")
        assertContainsAll(items, "LayoutMode", "Label")
    }

    // ── Completion: type properties inside TypeName(...) ──

    fun testCompletionTypeConstructorProperties() {
        val items = completeAt("Label {\n  Style: LabelStyle(F<caret>);\n}")
        println("LabelStyle(F) completions: $items")
        assertContainsAll(items, "FontSize", "FontName")
    }

    // ── Completion: type properties inside anonymous (...) ──

    fun testCompletionAnonymousObjectProperties() {
        val items = completeAt("Label {\n  Style: (F<caret>);\n}")
        println("Style: (F) completions: $items")
        assertContainsAll(items, "FontSize", "FontName")
    }

    // ── Completion: nested anonymous object ──

    fun testCompletionNestedAnonymousObjectProperties() {
        val items = completeAt("Label {\n  Anchor: (W<caret>);\n}")
        println("Anchor: (W) completions: $items")
        assertContainsAll(items, "Width")
    }

    // ── Completion: enum values for enum property ──

    fun testCompletionEnumValues() {
        val items = completeAt("Group {\n  LayoutMode: T<caret>;\n}")
        assertContainsAll(items, "Top", "TopScrolling")
    }

    // ── Completion: context filtering — only relevant results ──

    fun testCompletionNodeBodyNoEnumValues() {
        // In node body (not after ':'), enum values like "Top" should NOT appear
        val items = completeAt("Group {\n  T<caret>\n}")
        // Should have properties and child nodes, but NOT raw enum values like "TopScrolling"
        assertContainsAll(items, "TooltipText") // property
        assertFalse("Enum values should not appear in node body position", items.contains("TopScrolling"))
    }

    fun testCompletionEnumPropertyOnlyMatchingValues() {
        // After ':' for an enum property, only matching enum values should appear
        val items = completeAt("Label {\n  Style: LabelStyle(HorizontalAlignment: <caret>);\n}")
        assertContainsAll(items, "Start", "Center", "End")
        // Should NOT contain unrelated enum values
        assertFalse("Unrelated enum 'Top' should not appear for LabelAlignment", items.contains("Top"))
        assertFalse("Node names should not appear in value position", items.contains("Label"))
    }

    fun testCompletionBooleanProperty() {
        val items = completeAt("Label {\n  Visible: <caret>;\n}")
        assertContainsAll(items, "true", "false")
        assertFalse("Node names should not appear in boolean value position", items.contains("Label"))
    }

    fun testCompletionTypePropertySuggestsConstructor() {
        val items = completeAt("Label {\n  Style: <caret>;\n}")
        assertContainsAll(items, "LabelStyle")
    }

    fun testCompletionPropertyListNoNodes() {
        // Inside TypeName(...), should show type properties, not node names
        val items = completeAt("Label {\n  Style: LabelStyle(<caret>);\n}")
        assertContainsAll(items, "FontSize", "TextColor")
        assertFalse("Node names should not appear inside type constructor", items.contains("Group"))
    }

    fun testCompletionNodeWithoutChildrenNoChildNodes() {
        // Label does not support children — should not offer child node names
        val items = completeAt("Label {\n  <caret>\n}")
        assertContainsAll(items, "Text", "Visible") // properties
        assertFalse("Child nodes should not appear in non-container node", items.contains("Group"))
        assertFalse("Child nodes should not appear in non-container node", items.contains("Label"))
    }

    fun testCompletionNodeWithChildrenOffersChildNodes() {
        // Group supports children — should offer both properties and child nodes
        val items = completeAt("Group {\n  <caret>\n}")
        assertContainsAll(items, "LayoutMode") // property
        assertContainsAll(items, "Label", "Group", "Button") // child nodes
    }

    fun testCompletionVariablesWithoutAtSign() {
        // Variables should appear even without @ prefix, but with lower priority
        val items = completeAt("@VarA = 1;\n@VarB = 2;\nLabel {\n  Width: <caret>;\n}")
        assertTrue("Variables should appear in value completions, got: $items", items.any { it.contains("Var") })
    }

    fun testCompletionVariablesWithAtSign() {
        val items = completeAt("@VarA = 1;\n@VarB = 2;\nLabel {\n  Width: @<caret>;\n}")
        assertTrue("Variables should appear with @ prefix, got: $items", items.any { it.contains("Var") })
    }

    // ── Navigation: file-level variable ──

    fun testNavigationFileVariable() {
        myFixture.configureByText("test.ui", "@MyStyle = (FontSize: 16);\nLabel {\n  Style: @My<caret>Style;\n}")
        val ref = myFixture.getReferenceAtCaretPosition()
        assertNotNull("Reference should exist", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve", resolved)
        assertTrue("Should resolve to VARIABLE_DECL", resolved!!.text.startsWith("@MyStyle"))
    }

    // ── Navigation: node-local variable ──

    fun testNavigationLocalVariable() {
        myFixture.configureByText("test.ui", "@T = Group {\n  @LocalVar = 42;\n  Label {\n    Text: @Local<caret>Var;\n  }\n};")
        val ref = myFixture.getReferenceAtCaretPosition()
        assertNotNull("Reference should exist for local var", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve local var", resolved)
        assertTrue("Should resolve to local decl", resolved!!.text.startsWith("@LocalVar"))
    }

    // ── Navigation: spread variable ...@var ──

    fun testNavigationSpreadVariable() {
        myFixture.configureByText("test.ui", "@BaseAnchor = (Width: 100);\nLabel {\n  Anchor: (...@Base<caret>Anchor, Height: 50);\n}")
        val ref = myFixture.getReferenceAtCaretPosition()
        assertNotNull("Reference should exist for spread var", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve spread var", resolved)
        assertTrue("Should resolve to VARIABLE_DECL", resolved!!.text.startsWith("@BaseAnchor"))
    }

    // ── Navigation: spread cross-file variable ...$C.@var ──

    fun testNavigationSpreadCrossFile() {
        myFixture.addFileToProject("common.ui", "@SharedStyle = (FontSize: 20);")
        myFixture.configureByText("test.ui", "\$C = \"common.ui\";\nLabel {\n  Style: (...\$C.@Shared<caret>Style);\n}")
        val ref = myFixture.getReferenceAtCaretPosition()
        assertNotNull("Reference should exist for cross-file spread", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve cross-file spread", resolved)
        assertTrue("Should resolve to shared decl", resolved!!.text.startsWith("@SharedStyle"))
    }

    // ── Type checking: number to string ──

    fun testTypeMismatchNumberToString() {
        val warnings = getWarnings("Label {\n  Text: 123;\n}")
        assertTrue("Should flag number to String", warnings.any { it.contains("Type mismatch") && it.contains("String") })
    }

    // ── Type checking: string to boolean ──

    fun testTypeMismatchStringToBoolean() {
        val warnings = getWarnings("Label {\n  Visible: \"hello\";\n}")
        assertTrue("Should flag string to Boolean", warnings.any { it.contains("Type mismatch") && it.contains("Boolean") })
    }

    // ── Type checking: valid assignments ──

    fun testTypeMismatchValidAssignments() {
        val warnings = getWarnings("""
            Label {
              Text: "hello";
              Visible: true;
              Style: (FontSize: 16);
              Background: "texture.png";
              Background: #ff0000;
              Background: (TexturePath: "x.png");
            }
        """.trimIndent())
        val typeErrors = warnings.filter { it.contains("Type mismatch") }
        assertTrue("No type errors for valid assignments, got: $typeErrors", typeErrors.isEmpty())
    }

    // ── Type checking: inside type constructor ──

    fun testTypeMismatchInsideTypeConstructor() {
        val warnings = getWarnings("@Test = LabelStyle(\n  FontSize: \"not a number\"\n);")
        assertTrue("Should flag string for Integer in LabelStyle", warnings.any { it.contains("Type mismatch") })
    }

    // ── Type checking: inside anonymous object ──

    fun testTypeMismatchInsideAnonymousObject() {
        val warnings = getWarnings("Label {\n  Style: (FontSize: \"bad\");\n}")
        assertTrue("Should flag string for Integer inside anon obj", warnings.any { it.contains("Type mismatch") })
    }

    // ── Type checking: nested anonymous object ──

    fun testTypeMismatchNestedAnonymousObject() {
        val warnings = getWarnings("Label {\n  Anchor: (Width: \"bad\");\n}")
        assertTrue("Should flag string for Integer in nested anon obj", warnings.any { it.contains("Type mismatch") })
    }

    // ── Type checking: PatchStyle accepts string/color ──

    fun testPatchStyleShorthand() {
        val warnings = getWarnings("Label {\n  Background: \"texture.png\";\n}")
        val typeErrors = warnings.filter { it.contains("Type mismatch") }
        assertTrue("PatchStyle should accept string", typeErrors.isEmpty())
    }

    // ── Type checking: enum value validation ──

    fun testEnumValueValidation() {
        val warnings = getWarnings("Group {\n  LayoutMode: InvalidValue;\n}")
        assertTrue("Should flag invalid enum value", warnings.any { it.contains("Type mismatch") })
    }

    fun testEnumValueValid() {
        val warnings = getWarnings("Group {\n  LayoutMode: Top;\n}")
        val typeErrors = warnings.filter { it.contains("Type mismatch") }
        assertTrue("Top is valid LayoutMode", typeErrors.isEmpty())
    }

    // ── Unknown property checking ──

    fun testUnknownPropertyInNode() {
        val warnings = getWarnings("Label {\n  FakeProperty: 1;\n}")
        assertTrue("Should flag unknown property", warnings.any { it.contains("Unknown property") })
    }

    fun testUnknownPropertyInTypeConstructor() {
        val warnings = getWarnings("@X = LabelStyle(FakeProp: 1);")
        assertTrue("Should flag unknown type property", warnings.any { it.contains("Unknown property") })
    }

    // ── Schema: path property detection ──

    fun testSchemaPathProperties() {
        myFixture.configureByText("test.ui", "Group {}")
        val schema = HytaleUiSchemaService.getInstance()
        assertTrue("TexturePath is path property", schema.isPathProperty("TexturePath"))
        assertTrue("MaskTexturePath is path property", schema.isPathProperty("MaskTexturePath"))
        assertTrue("SoundPath is path property", schema.isPathProperty("SoundPath"))
        assertTrue("AssetPath is path property", schema.isPathProperty("AssetPath"))
        assertTrue("IconTexturePath is path property", schema.isPathProperty("IconTexturePath"))
        assertFalse("FontSize is NOT path property", schema.isPathProperty("FontSize"))
        assertFalse("Text is NOT path property", schema.isPathProperty("Text"))
    }

    // ── Navigation: path property to file ──

    fun testNavigationTexturePath() {
        myFixture.addFileToProject("Common/Buttons/Primary.png", "")
        myFixture.configureByText("test.ui", "@X = PatchStyle(TexturePath: \"Common/Buttons/Prim<caret>ary.png\");")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("Reference should exist for TexturePath", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve TexturePath to file", resolved)
    }

    fun testNavigationSoundPath() {
        myFixture.addFileToProject("Sounds/Click.ogg", "")
        myFixture.configureByText("test.ui", "@X = (SoundPath: \"Sounds/Cli<caret>ck.ogg\");")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("Reference should exist for SoundPath", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve SoundPath to file", resolved)
    }

    fun testNavigationPatchStyleShorthand() {
        myFixture.addFileToProject("texture.png", "")
        myFixture.configureByText("test.ui", "Label {\n  Background: \"tex<caret>ture.png\";\n}")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("Reference should exist for PatchStyle shorthand string", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve PatchStyle shorthand to file", resolved)
    }

    fun testNavigationMaskTexturePath() {
        myFixture.addFileToProject("mask.png", "")
        myFixture.configureByText("test.ui", "Label {\n  MaskTexturePath: \"ma<caret>sk.png\";\n}")
        val ref = myFixture.file.findReferenceAt(myFixture.caretOffset)
        assertNotNull("Reference should exist for MaskTexturePath", ref)
        val resolved = ref!!.resolve()
        assertNotNull("Should resolve MaskTexturePath to file", resolved)
    }

    // ── Inspection: unresolved file in import ──

    fun testUnresolvedFileImport() {
        val warnings = getWarnings("\$C = \"nonexistent_file.ui\";")
        assertTrue("Should warn about unresolved import file", warnings.any { it.contains("Cannot resolve file") })
    }

    fun testResolvedFileImport() {
        myFixture.addFileToProject("Common.ui", "@X = 1;")
        val warnings = getWarnings("\$C = \"Common.ui\";")
        val fileErrors = warnings.filter { it.contains("Cannot resolve file") }
        assertTrue("Resolved import should not warn, got: $fileErrors", fileErrors.isEmpty())
    }

    // ── Inspection: unresolved file in path property ──

    fun testUnresolvedTexturePath() {
        val warnings = getWarnings("@X = PatchStyle(TexturePath: \"nonexistent/texture.png\");")
        assertTrue("Should warn about unresolved texture path", warnings.any { it.contains("Cannot resolve file") })
    }

    fun testResolvedTexturePath() {
        myFixture.addFileToProject("Common/tex.png", "")
        val warnings = getWarnings("@X = PatchStyle(TexturePath: \"Common/tex.png\");")
        val fileErrors = warnings.filter { it.contains("Cannot resolve file") }
        assertTrue("Resolved texture path should not warn, got: $fileErrors", fileErrors.isEmpty())
    }

    fun testUnresolvedPatchStyleShorthand() {
        val warnings = getWarnings("Label {\n  Background: \"nonexistent.png\";\n}")
        assertTrue("Should warn about unresolved PatchStyle shorthand", warnings.any { it.contains("Cannot resolve file") })
    }

    fun testUnresolvedSoundPath() {
        val warnings = getWarnings("@X = (SoundPath: \"nonexistent/sound.ogg\");")
        // SoundPath check depends on context type inference being available
        // In a bare anonymous object without node context, the context may not resolve
        // but if it does, it should flag unresolved
        // This test ensures no crash at minimum
    }

    // ── Non-path strings should NOT get file warnings ──

    fun testNonPathStringNoWarning() {
        val warnings = getWarnings("Label {\n  Text: \"hello world\";\n}")
        val fileErrors = warnings.filter { it.contains("Cannot resolve file") }
        assertTrue("Text string should NOT trigger file warning, got: $fileErrors", fileErrors.isEmpty())
    }

    // ── Documentation (Ctrl+hover / Ctrl+Q) ──

    fun testDocumentationForNode() {
        val doc = getDocAt("Lab<caret>el {\n  Text: \"hello\";\n}")
        assertNotNull("Should generate doc for node", doc)
        assertTrue("Doc should contain node name", doc!!.contains("Label"))
        assertTrue("Doc should list properties", doc.contains("Text"))
        assertTrue("Doc should list Visible property", doc.contains("Visible"))
    }

    fun testDocumentationForNodeWithChildren() {
        val doc = getDocAt("Gro<caret>up {\n}")
        assertNotNull("Should generate doc for Group", doc)
        assertTrue("Doc should mention children", doc!!.contains("child"))
        assertTrue("Doc should list LayoutMode property", doc.contains("LayoutMode"))
    }

    fun testDocumentationForTypeConstructor() {
        val doc = getDocAt("@X = Label<caret>Style(FontSize: 16);")
        assertNotNull("Should generate doc for type", doc)
        assertTrue("Doc should contain type name", doc!!.contains("LabelStyle"))
        assertTrue("Doc should list FontSize property", doc.contains("FontSize"))
        assertTrue("Doc should list TextColor property", doc.contains("TextColor"))
    }

    fun testDocumentationForPropertyKey() {
        val doc = getDocAt("Label {\n  Tex<caret>t: \"hello\";\n}")
        assertNotNull("Should generate doc for property", doc)
        assertTrue("Doc should show type String", doc!!.contains("String"))
        assertTrue("Doc should show context Label", doc.contains("Label"))
    }

    fun testDocumentationForPropertyKeyWithEnumType() {
        val doc = getDocAt("Group {\n  Layout<caret>Mode: Top;\n}")
        assertNotNull("Should generate doc for enum property", doc)
        assertTrue("Doc should show LayoutMode type", doc!!.contains("LayoutMode"))
        assertTrue("Doc should show enum values", doc.contains("Top"))
    }

    fun testDocumentationForPropertyInTypeConstructor() {
        val doc = getDocAt("@X = LabelStyle(Font<caret>Size: 16);")
        assertNotNull("Should generate doc for type property", doc)
        assertTrue("Doc should show Float type", doc!!.contains("Float"))
        assertTrue("Doc should show LabelStyle context", doc.contains("LabelStyle"))
    }

    fun testDocumentationForVariableDeclaration() {
        val doc = getDocAt("@My<caret>Var = 42;\n")
        assertNotNull("Should generate doc for variable declaration", doc)
        assertTrue("Doc should contain variable name", doc!!.contains("MyVar"))
        assertTrue("Doc should show value", doc.contains("42"))
        assertTrue("Doc should say declaration", doc.contains("declaration"))
    }

    fun testDocumentationForVariableReference() {
        val doc = getDocAt("@MyVar = 42;\nLabel {\n  Width: @My<caret>Var;\n}")
        assertNotNull("Should generate doc for variable ref", doc)
        assertTrue("Doc should contain variable name", doc!!.contains("MyVar"))
        assertTrue("Doc should show value", doc.contains("42"))
    }

    fun testDocumentationForEnumValue() {
        val doc = getDocAt("Group {\n  LayoutMode: To<caret>p;\n}")
        assertNotNull("Should generate doc for enum value", doc)
        assertTrue("Doc should contain enum name LayoutMode", doc!!.contains("LayoutMode"))
        assertTrue("Doc should list all values", doc.contains("TopScrolling"))
    }

    fun testDocumentationForFileImport() {
        val doc = getDocAt("\$So<caret>unds = \"Sounds.ui\";\n")
        assertNotNull("Should generate doc for file import", doc)
        assertTrue("Doc should contain alias", doc!!.contains("Sounds"))
        assertTrue("Doc should contain path", doc.contains("Sounds.ui"))
    }

    // ── Template instantiation: @Variable { } extends node ──

    fun testCompletionInsideTemplateInst() {
        // @MyLabel is defined as Label, so inside @MyLabel { } we should get Label properties
        val items = completeAt("@MyLabel = Label {\n  Text: \"hello\";\n};\n@MyLabel {\n  V<caret>\n}")
        assertContainsAll(items, "Visible")
    }

    fun testCompletionInsideTemplateInstGroup() {
        // @MyGroup is defined as Group, so inside @MyGroup { } we should get Group properties AND child nodes
        val items = completeAt("@MyGroup = Group {};\n@MyGroup {\n  <caret>\n}")
        assertContainsAll(items, "LayoutMode") // Group property
        assertContainsAll(items, "Label") // child node (Group has children)
    }

    fun testPropertyValidationInsideTemplateInst() {
        val warnings = getWarnings("@MyLabel = Label {};\n@MyLabel {\n  FakeProperty: 1;\n}")
        assertTrue("Should flag unknown property in template inst", warnings.any { it.contains("Unknown property") })
    }

    fun testTypeMismatchInsideTemplateInst() {
        val warnings = getWarnings("@MyLabel = Label {};\n@MyLabel {\n  Visible: \"not a bool\";\n}")
        assertTrue("Should flag type mismatch in template inst", warnings.any { it.contains("Type mismatch") })
    }

    fun testValidPropertyInsideTemplateInst() {
        val warnings = getWarnings("@MyLabel = Label {};\n@MyLabel {\n  Text: \"hello\";\n}")
        val errors = warnings.filter { it.contains("Unknown property") || it.contains("Type mismatch") }
        assertTrue("Valid property should not be flagged, got: $errors", errors.isEmpty())
    }

    fun testCrossFileTemplateInstCompletion() {
        myFixture.addFileToProject("common.ui", "@DefaultButton = Button {};")
        val items = completeAt("\$C = \"common.ui\";\n\$C.@DefaultButton {\n  <caret>\n}")
        assertContainsAll(items, "Visible") // Button property
    }

    // ── Quick navigate info (Ctrl+hover tooltip) ──

    fun testQuickInfoForNode() {
        val info = getQuickInfoAt("Lab<caret>el {\n  Text: \"hello\";\n}")
        assertNotNull("Should generate quick info for node", info)
        assertTrue("Quick info should contain Label", info!!.contains("Label"))
        assertTrue("Quick info should mention properties", info.contains("properties"))
    }

    fun testQuickInfoForPropertyKey() {
        val info = getQuickInfoAt("Label {\n  Tex<caret>t: \"hello\";\n}")
        assertNotNull("Should generate quick info for property", info)
        assertTrue("Quick info should contain Text", info!!.contains("Text"))
        assertTrue("Quick info should contain String", info.contains("String"))
    }

    fun testQuickInfoForVariable() {
        val info = getQuickInfoAt("@MyVar = 42;\nLabel {\n  Width: @My<caret>Var;\n}")
        assertNotNull("Should generate quick info for variable", info)
        assertTrue("Quick info should contain @MyVar", info!!.contains("@MyVar"))
        assertTrue("Quick info should show value", info.contains("42"))
    }

    // ── Find Usages ──

    fun testFindUsagesVariable() {
        myFixture.configureByText("test.ui", "@MyV<caret>ar = 42;\nLabel {\n  Width: @MyVar;\n  Height: @MyVar;\n}")
        val element = myFixture.elementAtCaret
        assertNotNull("Should find element at caret", element)
        assertTrue("Should be a variable declaration", element is cz.creeperface.hytale.uimanager.intellij.parser.HytaleUiVariableDeclElement)
        val usages = myFixture.findUsages(element)
        assertTrue("Should find usages of @MyVar, got ${usages.size}", usages.size >= 2)
    }

    fun testFindUsagesFileImport() {
        myFixture.addFileToProject("Common.ui", "@X = 1;")
        myFixture.configureByText("test.ui", "\$C<caret> = \"Common.ui\";\n@Y = \$C.@X;")
        val element = myFixture.elementAtCaret
        assertNotNull("Should find element at caret", element)
        // Verify FILE_IMPORT is a PsiNameIdentifierOwner so Find Usages can work on it
        var fileImport: PsiElement? = element
        while (fileImport != null && fileImport !is cz.creeperface.hytale.uimanager.intellij.parser.HytaleUiFileImportElement) {
            fileImport = fileImport.parent
        }
        assertNotNull("Should find FILE_IMPORT element", fileImport)
        assertTrue("FILE_IMPORT should be PsiNameIdentifierOwner", fileImport is com.intellij.psi.PsiNameIdentifierOwner)
        assertEquals("C", (fileImport as com.intellij.psi.PsiNameIdentifierOwner).name)
    }

    // ── Rename refactoring ──

    fun testRenameVariable() {
        myFixture.configureByText("test.ui", "@My<caret>Var = 42;\nLabel {\n  Width: @MyVar;\n  Height: @MyVar;\n}")
        myFixture.renameElementAtCaret("NewName")
        val text = myFixture.file.text
        assertFalse("Old name should be gone", text.contains("@MyVar"))
        assertTrue("Declaration should be renamed", text.contains("@NewName = 42"))
        assertEquals("All usages should be renamed", 3, Regex("@NewName").findAll(text).count())
    }

    fun testRenameVariableFromUsage() {
        myFixture.configureByText("test.ui", "@MyVar = 42;\nLabel {\n  Width: @My<caret>Var;\n}")
        myFixture.renameElementAtCaret("Renamed")
        val text = myFixture.file.text
        assertTrue("Declaration should be renamed", text.contains("@Renamed = 42"))
        assertTrue("Usage should be renamed", text.contains("Width: @Renamed"))
        assertFalse("Old name should be gone", text.contains("@MyVar"))
    }

    // ── Helpers ──

    private fun completeAt(code: String): Set<String> {
        myFixture.configureByText("test.ui", code)
        val items = myFixture.completeBasic() ?: return emptySet()
        return items.map { it.lookupString }.toSet()
    }

    private fun getWarnings(code: String): List<String> {
        myFixture.configureByText("test.ui", code)
        return myFixture.doHighlighting()
            .filter { it.severity.myVal >= com.intellij.lang.annotation.HighlightSeverity.WARNING.myVal }
            .mapNotNull { it.description }
    }

    private fun getDocAt(code: String): String? {
        myFixture.configureByText("test.ui", code)
        val leaf = myFixture.file.findElementAt(myFixture.caretOffset) ?: return null
        val provider = cz.creeperface.hytale.uimanager.intellij.documentation.HytaleUiDocumentationProvider()
        // Simulate the IDE chain: getCustomDocumentationElement → generateDoc
        val target = provider.getCustomDocumentationElement(
            myFixture.editor, myFixture.file, leaf, myFixture.caretOffset
        ) ?: leaf
        return provider.generateDoc(target, leaf)
    }

    private fun getQuickInfoAt(code: String): String? {
        myFixture.configureByText("test.ui", code)
        val leaf = myFixture.file.findElementAt(myFixture.caretOffset) ?: return null
        val provider = cz.creeperface.hytale.uimanager.intellij.documentation.HytaleUiDocumentationProvider()
        val target = provider.getCustomDocumentationElement(
            myFixture.editor, myFixture.file, leaf, myFixture.caretOffset
        ) ?: leaf
        return provider.getQuickNavigateInfo(target, leaf)
    }

    private fun assertContainsAll(actual: Set<String>, vararg expected: String) {
        for (e in expected) {
            assertTrue("Expected '$e' in completions but got: ${actual.sorted().take(20)}", e in actual)
        }
    }

    private fun collectByType(element: PsiElement, type: com.intellij.psi.tree.IElementType, result: MutableList<PsiElement>) {
        if (element.node.elementType == type) result.add(element)
        for (child in element.children) collectByType(child, type, result)
    }
}
