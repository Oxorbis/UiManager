package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile

/**
 * Resolves relative file paths used in .ui file imports and resource paths.
 * Paths are relative to the importing file's directory (for imports) or
 * resolved against project content roots and library roots (for asset paths).
 */
object HytaleUiFileResolver {

    /**
     * Resolves a relative import path to a VirtualFile.
     * First tries relative to the importing file's directory,
     * then falls back to content roots and library roots.
     *
     * @param fromFile the file containing the import statement
     * @param relativePath the relative path (e.g., "../Common.ui" or "Sounds/Click.ogg")
     * @return the resolved VirtualFile, or null if not found
     */
    fun resolveImportPath(fromFile: PsiFile, relativePath: String): VirtualFile? {
        // 1. Try relative to the containing file's directory
        val containingDir = fromFile.virtualFile?.parent
        if (containingDir != null) {
            val resolved = resolveRelativePath(containingDir, relativePath)
            if (resolved != null) return resolved
        }

        // 2. Try content roots and library roots
        return resolveInRoots(fromFile.project, relativePath)
    }

    /**
     * Resolves a resource path (texture, sound, asset) against all known roots.
     * These paths are not necessarily relative to the current file — they can
     * reference files inside JAR libraries (other Hytale plugins, base server).
     *
     * @param fromFile the file containing the path reference
     * @param resourcePath the resource path (e.g., "Common/Buttons/Primary.png")
     * @return the resolved VirtualFile, or null if not found
     */
    fun resolveResourcePath(fromFile: PsiFile, resourcePath: String): VirtualFile? {
        // 1. Try relative to the containing file's directory
        val containingDir = fromFile.virtualFile?.parent
        if (containingDir != null) {
            val resolved = resolveRelativePath(containingDir, resourcePath)
            if (resolved != null) return resolved
        }

        // 2. Try all content roots and library roots
        return resolveInRoots(fromFile.project, resourcePath)
    }

    /**
     * Resolves a path against project content roots and library class roots (including JARs).
     */
    private fun resolveInRoots(project: Project, path: String): VirtualFile? {
        // Search project content roots
        val enumerator = OrderEnumerator.orderEntries(project).recursively()

        for (root in enumerator.sourceRoots) {
            val resolved = resolveRelativePath(root, path)
            if (resolved != null) return resolved
        }

        // Search library roots (JARs from other plugins, base server)
        for (root in enumerator.classesRoots) {
            val resolved = resolveRelativePath(root, path)
            if (resolved != null) return resolved
        }

        // Also try project resource roots
        val projectDir = project.guessProjectDir()
        if (projectDir != null) {
            // Common pattern: resources are in src/main/resources/
            val resourceDirs = listOf(
                projectDir.findFileByRelativePath("src/main/resources"),
                projectDir,
            )
            for (dir in resourceDirs) {
                if (dir != null) {
                    val resolved = resolveRelativePath(dir, path)
                    if (resolved != null) return resolved
                }
            }
        }

        return null
    }

    /**
     * Resolves a relative path from a given directory.
     */
    fun resolveRelativePath(fromDir: VirtualFile, relativePath: String): VirtualFile? {
        val parts = relativePath.split("/")
        var current: VirtualFile? = fromDir

        for (part in parts) {
            current = when (part) {
                ".." -> current?.parent
                "." -> current
                "" -> current
                else -> current?.findChild(part)
            }
            if (current == null) return null
        }

        return current
    }

    /**
     * Finds all .ui files in the project that could be referenced.
     * Useful for completion of import paths.
     */
    fun findUiFilesInProject(fromFile: PsiFile): List<VirtualFile> {
        val result = mutableListOf<VirtualFile>()
        val projectDir = fromFile.project.guessProjectDir() ?: return result
        collectUiFiles(projectDir, result)
        return result
    }

    private fun collectUiFiles(dir: VirtualFile, result: MutableList<VirtualFile>) {
        for (child in dir.children) {
            if (child.isDirectory) {
                collectUiFiles(child, result)
            } else if (child.extension == "ui") {
                result.add(child)
            }
        }
    }
}
