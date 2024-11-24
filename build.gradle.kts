plugins {
    java
    `java-library`
    id("application")
    id("com.gradleup.shadow") version "8.3.5"
    id("org.checkerframework") version "0.6.46"
    id("com.diffplug.spotless") version "7.0.0.BETA4"
    id("com.github.node-gradle.node") version "7.1.0"
}

apply(plugin = "org.checkerframework")

group = "me.brandonli"
version = "v1.0.0-SNAPSHOT"
description = "AllerExplore Back End"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.camel.barcode)
    implementation(libs.jsoup)
    implementation(libs.openfoodfacts.java.wrapper)
    implementation(libs.javalin)
    implementation(libs.caffeine)
    implementation(libs.sqlite.jdbc)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    val language = JavaLanguageVersion.of(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    toolchain.languageVersion.set(language)
}

val windows = System.getProperty("os.name").lowercase().contains("windows")
tasks {

    application {
        mainClass.set("me.brandonli.allerexplore.AllerExplore")
    }

    withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-parameters")
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
        options.isFork = true
        options.forkOptions.memoryMaximumSize = "4g"
    }

    assemble {
        dependsOn("shadowJar")
    }

    build {
        dependsOn("spotlessApply")
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        filteringCharset = "UTF-8"
    }

    spotlessInternalRegisterDependencies {
        dependsOn("nodeSetup", "npmSetup")
    }

    sourceSets {
        main {
            java.srcDir("src/main/java")
            resources.srcDir("src/main/resources")
        }
    }

    spotless {
        java {
            importOrder()
            removeUnusedImports()
            prettier(mapOf("prettier" to "3.3.3", "prettier-plugin-java" to "2.6.4"))
                .config(mapOf("parser" to "java",
                    "tabWidth" to 2,
                    "plugins" to listOf("prettier-plugin-java"),
                    "printWidth" to 140))
                .nodeExecutable(provider { setupNodeEnvironment() })
            licenseHeaderFile("HEADER")
        }
    }

    checkerFramework {
        checkers = listOf("org.checkerframework.checker.nullness.NullnessChecker")
        extraJavacArgs = listOf(
            "-AsuppressWarnings=uninitialized",
            "-Astubs=${project.file("checker-framework")}"
        )
    }

    node {
        download = true
        version = "22.9.0"
        workDir = file("build/nodejs")
    }
}

fun setupNodeEnvironment(): File {
    val npmExec = if (windows) "node.exe" else "bin/node"
    val folder = node.resolvedNodeDir.get()
    val executable = folder.file(npmExec).asFile
    return executable
}