include(
    "android",
    "asset",
    "core:common",
    "server:launcher"
)

dependencyResolutionManagement {
    val kotlinVersion = "1.6.20"
    val ktxVersion = "1.10.0-b1"
    val gdxVersion = "1.10.0"
    val ashleyVersion = "1.7.4"
    val aiVersion = "1.8.2"
    val gradleTool = "7.0.4"

    versionCatalogs {
        create("libs") {
            library("tgradle", "com.android.tools.build:gradle:$gradleTool")
            library("kgradle", "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            library("kstdlib", "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

            library("kapp", "io.github.libktx:ktx-app:$ktxVersion")
            library("kmath", "io.github.libktx:ktx-math:$ktxVersion")

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