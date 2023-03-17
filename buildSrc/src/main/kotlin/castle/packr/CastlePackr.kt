package castle.packr

import com.badlogicgames.packr.Packr
import com.badlogicgames.packr.PackrConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.*

abstract class CastlePackr : DefaultTask() {
    @TaskAction
    fun packr() {
        val buildDir = project.buildDir
        val windowsPackerFolder = "win-out"
        File("$buildDir/libs/$windowsPackerFolder").deleteRecursively()

        val config = PackrConfig()
        config.platform = PackrConfig.Platform.Windows64
        config.jdk = getProp()
        config.executable = "simple-castle"
        config.classpath = listOf("$buildDir/libs/simple-castle.jar")
        config.removePlatformLibs = config.classpath
        config.mainClass = "castle.core.app.ServerLauncher"
        config.minimizeJre = "hard"
        config.vmArgs = listOf();
        config.outDir = File("$buildDir/libs/$windowsPackerFolder")
        config.useZgcIfSupportedOs = true

        Packr().pack(config)
    }

    private fun getProp(): String {
        val props = CastlePackr::class.java.classLoader.getResourceAsStream("jdk.properties").use {
            Properties().apply { load(it) }
        }
        return (props.getProperty("winJdkPath") as String)
    }
}