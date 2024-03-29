import castle.packr.CastlePackr
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly

val assetsPath: String = rootProject.file("android/assets").path
val os = (System.getProperties()["os.name"] as String).toLowerCaseAsciiOnly()
val gameClassName: String = "castle.core.app.ServerLauncher"

plugins {
    application
}

application {
    mainClass.set(gameClassName)
    tasks.run.get().workingDir = File(assetsPath)
    if (OperatingSystem.current().isMacOsX) {
        applicationDefaultJvmArgs += "-XstartOnFirstThread"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of("17"))
    }
}

sourceSets {
    main {
        resources {
            srcDir(assetsPath)
        }
    }
}

tasks.jar {
    dependsOn(":core:common:jar")
    archiveBaseName.set("simple-castle")
    from(configurations.runtimeClasspath.get().map(::zipTree))

    manifest.attributes["Main-Class"] = gameClassName
    manifest.attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(separator = " ") { it.name }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<CastlePackr>("distpackr") {
    dependsOn(":server:launcher:jar")
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.kstdlib)
    implementation(libs.kapp)
    implementation(libs.gbackend)
    implementation(libs.glwjgl3)
    implementation(libs.koin)

    implementation(variantOf(libs.gplatformdesktop) { classifier("natives-desktop") })
    implementation(variantOf(libs.gplatformbullet) { classifier("natives-desktop") })
    implementation(variantOf(libs.gplatformfreetype) { classifier("natives-desktop") })
}