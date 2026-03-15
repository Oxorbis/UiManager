plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "cz.creeperface.hytale.uimanager.intellij"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        pluginVerifier()
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    pluginConfiguration {
        id = "cz.creeperface.hytale.uimanager.intellij"
        name = "Hytale UI Support"
        version = "1.0.0"
        description = "Language support for Hytale .ui files: syntax highlighting, autocompletion, navigation, and inspections."
        ideaVersion {
            sinceBuild = "243"
            untilBuild = provider { null }
        }
    }
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
