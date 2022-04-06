import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.konan.file.file
import java.net.URI
import java.nio.file.FileSystems

plugins {
    `maven-publish`
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "7.1.2"

    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
}

version = project.property("mod_version") as? String ?: "1.0.0"
group = project.property("maven_group") as? String ?: "dev.koding"

repositories {
    mavenCentral()
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.terraformersmc.com")
    maven("https://repo.sk1er.club/repository/maven-public")
}

dependencies {
    // Required fabric dependencies
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")

    // Dev Mods
    modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:1.0.0")
    modImplementation("maven.modrinth:lazydfu:0.1.2")
    modImplementation("com.terraformersmc:modmenu:3.1.0")

    // Kotlin
    modImplementation(kotlin("stdlib-jdk8"))
    library("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.property("kotlin_serialization_version")}")

    // Essential
    modImplementation("gg.essential:loader-fabric:1.0.0")
    modImplementation("gg.essential:essential-1.18.1-fabric:2289")

    // Other
    library("io.github.microutils:kotlin-logging-jvm:2.1.21") {
        exclude(group = "org.jetbrains.kotlin")
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/celeste.accesswidener"))

    runs {
        getByName("client") {
            vmArgs += arrayOf("-XX:+AllowEnhancedClassRedefinition", "-XX:HotswapAgent=fatjar")
            property("essential.autoUpdate", "false")
        }
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        configurations = listOf(project.configurations.shadow.get())
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.property("version"),
                "mc_version" to project.property("minecraft_version")
            )
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.property("archives_base_name")}" }
        }
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.remapJar) {
                builtBy(tasks.remapJar)
            }
        }
    }
}

fun DependencyHandlerScope.library(id: Any, build: ExternalModuleDependency.() -> Unit = {}) {
    implementation(id.toString()) {
        exclude(group = "org.slf4j")
        exclude(group = "com.google.code.gson")
        exclude(group = "org.ow2.asm")
        this.build()
    }
    shadow(id.toString()) {
        exclude(group = "org.slf4j")
        exclude(group = "com.google.code.gson")
        exclude(group = "org.ow2.asm")
        this.build()
    }
}

// Essential Patcher

val patchEssential: Task by tasks.creating {
    doLast {
        val target = file("run/essential/Essential (fabric_1.18.2).jar")

        val originalFile = file("build/patched/essential-original.jar")
        val output = file("build/patched/essential.jar")

        if (!output.exists()) {
            println("Patched file already exists, skipping patch")

            if (!target.exists()) {
                throw RuntimeException("Essential is not installed!")
            }

            val deleteTargets = arrayOf("kotlin", "kotlinx")
            target.copyTo(originalFile, true)
            target.copyTo(output, true)

            FileSystems.newFileSystem(URI("jar", output.toURI().toString(), null), mapOf("create" to "false"))
                .use { fs -> deleteTargets.forEach { fs.file(it).deleteRecursively() } }
        }

        // Copy over patched JAR
        output.copyTo(target, true)
    }
}