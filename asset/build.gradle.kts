import java.io.FileInputStream
import java.util.*

val propertiesFile = rootProject.file("/asset/paths.properties")
val properties = Properties().apply { load(FileInputStream(propertiesFile)) }
val blenderPath = "\"${properties.getProperty("blender-path")}\""
val fbxPath = "\"${properties.getProperty("fbx-path")}\""
val texturesFolder = rootProject.file("/asset/blender-project/dynamic-loaded/textures")
val asset3dFolder = rootProject.file("/android/assets/assets3d")
val pythonScriptFile = "\"${rootProject.file("/asset/python/blender-export-fbx.py")}\""
val models = "/asset/blender-project/dynamic-loaded"

task("buildModel") {
    doFirst {
        val files: Array<File> = rootProject.file(models).listFiles()!!
        val models = files.filter { it.isFile && it.extension == "blend" }
        for (model in models) {
            val blendFile = "\"${model.path}\""
            val targetFileFbx = "\"${rootProject.file("/asset/blender-project/dynamic-loaded/${model.nameWithoutExtension}.fbx")}\""
            exec {
                commandLine(blenderPath, blendFile, "--background", "--python", pythonScriptFile, "--", "filename=$targetFileFbx")
            }
            exec {
                commandLine(fbxPath, "-f", "-o", "G3DJ", targetFileFbx)
            }
        }
    }
}
task("copyModel") {
    doFirst {
        val files: Array<File> = rootProject.file(models).listFiles()!!
        val models = files.filter { it.isFile && it.extension == "g3dj" }
        for (model in models) {
            copy {
                from(model, texturesFolder)
                into(asset3dFolder)
            }
        }
    }
    doLast {
        val files: Array<File> = rootProject.file(models).listFiles()!!
        val toDelete = listOf("fbx", "g3dj", "blend1")
        val models = files.filter { it.isFile && toDelete.contains(it.extension) }
        delete(models)
        delete(texturesFolder)
    }
}

tasks.getByName("buildModel").finalizedBy("copyModel")