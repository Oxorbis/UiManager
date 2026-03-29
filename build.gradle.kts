import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    // id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

group = "cz.creeperface.hytale.uimanager"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.pokeskies.com/releases")
    maven("https://maven.hytale-modding.info/releases")
}

dependencies {
    compileOnly(kotlin("reflect"))
    val libs = fileTree(rootProject.file("libs")).include("*.jar")
    compileOnly(libs)
    testImplementation(libs)

    val userHomeDir = file(System.getProperty("user.home"))
    val hytaleServerJar = files("${userHomeDir.absolutePath}/Library/Application Support/Hytale/install/release/package/game/latest/Server/HytaleServer.jar")
    compileOnly(hytaleServerJar)
    testImplementation(hytaleServerJar)

    compileOnly("aster.amo:kytale:1.4.4")

    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("com.google.code.gson:gson:2.11.0")

    testImplementation(kotlin("test"))
}

sourceSets {
    create("uigen") {
        kotlin.srcDir("src/uigen/kotlin")
    }
}

dependencies {
    "uigenImplementation"("com.squareup:kotlinpoet:2.0.0")
    "uigenImplementation"("com.google.code.gson:gson:2.11.0")
    "uigenImplementation"(kotlin("stdlib"))
}

tasks.register<JavaExec>("generateUi") {
    group = "generation"
    description = "Generates UI classes from ui_structure_report.json"
    classpath = sourceSets["uigen"].runtimeClasspath
    mainClass.set("cz.creeperface.hytale.uimanager.UiGeneratorKt")
    args("src/uigen/resources/ui_structure_report.json", "src/main/kotlin")
}

tasks.register<JavaExec>("testUi") {
    group = "generation"
    description = "Runs test code"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("cz.creeperface.hytale.uimanager.UiTestKt")
}

tasks.register<JavaExec>("runMain") {
    group = "application"
    description = "Runs the Main.kt"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("cz.creeperface.hytale.uimanager.MainKt")
}

tasks.jar {
    from(
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") && !it.name.startsWith("kotlin-stdlib") && !it.name.startsWith("kotlin") }
            .map { zipTree(it) }
    )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

kotlin {
    jvmToolchain(24)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

tasks.test {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters"))
}