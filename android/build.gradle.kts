plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "castle.core"

    buildToolsVersion("29.0.3")
    compileSdkVersion(31)
    sourceSets {
        named("main") {
            res.srcDir("res")
            jniLibs.srcDir("libs")
        }
    }
    defaultConfig {
        val appVersion = "1.0.0"
        applicationId = "com.example.android"
        minSdkVersion(14)
        targetSdkVersion(31)
        versionCode = appVersion.split('.').joinToString("") { it.padStart(2, '0') }.toInt()
        versionName = appVersion
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

val natives: Configuration by configurations.creating

dependencies {
    val gdxVersion = "1.10.0"
    val kotlinVersion = "1.6.20"

    implementation(project(":core"))
    api("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
    api("com.badlogicgames.gdx:gdx-bullet:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-armeabi")
    natives("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-x86_64")
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
}

tasks.register("copyAndroidNatives") {
    doFirst {
        natives.files.forEach { jar ->
            val outputDir = file("libs/" + jar.nameWithoutExtension.substringAfterLast("natives-"))
            outputDir.mkdirs()
            copy {
                from(zipTree(jar))
                into(outputDir)
                include("*.so")
            }
        }
    }
}
tasks.whenTaskAdded {
    if ("package" in name) {
        dependsOn("copyAndroidNatives")
    }
}