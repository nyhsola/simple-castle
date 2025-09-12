include(
    "android",
    "asset",
    "core:common",
    "server:launcher"
)

pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    }
}

dependencyResolutionManagement {
    val kotlinVersion = "2.1.0"
    val ktxVersion = "1.13.1-rc1"
    val gdxVersion = "1.13.5"
    val ashleyVersion = "1.7.4"
    val aiVersion = "1.8.2"
    val gradleTool = "8.13.0"
    val koinVersion = "4.1.1"
    val koinAnnotationVersion = "2.2.0-RC1"

    versionCatalogs {
        create("libs") {
            library("tgradle", "com.android.tools.build:gradle:$gradleTool")
            library("kgradle", "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            library("kstdlib", "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

            library("kapp", "io.github.libktx:ktx-app:$ktxVersion")
            library("kmath", "io.github.libktx:ktx-math:$ktxVersion")

            library("koin", "io.insert-koin:koin-core:$koinVersion")
            library("koina", "io.insert-koin:koin-annotations:$koinAnnotationVersion")
            library("koinc", "io.insert-koin:koin-ksp-compiler:$koinAnnotationVersion")

            library("gashley", "com.badlogicgames.ashley:ashley:$ashleyVersion")
            library("gai", "com.badlogicgames.gdx:gdx-ai:$aiVersion")
            library("gfreetype", "com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
            library("gbullet", "com.badlogicgames.gdx:gdx-bullet:$gdxVersion")
            library("gbackend", "com.badlogicgames.gdx:gdx-backend-headless:$gdxVersion")
            library("glwjgl3", "com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")

            library("gplatformdesktop", "com.badlogicgames.gdx:gdx-platform:$gdxVersion")
            library("gplatformbullet", "com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion")
            library("gplatformfreetype", "com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion")
        }
    }
}