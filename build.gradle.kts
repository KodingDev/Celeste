import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `maven-publish`
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "7.1.2"

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
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
    val library: (id: String) -> Unit = {
        implementation(it) {
            exclude(group = "org.slf4j")
            exclude(group = "com.google.code.gson")
            exclude(group = "org.ow2.asm")
        }
        shadow(it) {
            exclude(group = "org.slf4j")
            exclude(group = "com.google.code.gson")
            exclude(group = "org.ow2.asm")
        }
    }

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
    library("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.property("kotlin_serialization_version")}")

    // Essential
    modRuntimeOnly("gg.essential:loader-fabric:1.0.0")
    modCompileOnly("gg.essential:essential-1.18.1-fabric:2289")
}

loom {
    accessWidenerPath.set(file("src/main/resources/celeste.accesswidener"))
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
