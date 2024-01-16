import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

object ProjectSettings {

    private const val MAYOR_VERSION = 0
    private const val MINOR_VERSION = 0

    // git rev-list --first-parent --count origin/master +1
    private const val VERSION_DIF = 0
    private const val BASE_VERSION_CODE = 0

    const val PROJECT_ID = "com.oztechan.tracefit"

    const val COMPILE_SDK_VERSION = 34
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION = 34

    val JAVA_VERSION = JavaVersion.VERSION_17

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getVersionCode(project: Project) = try {
        gitCommitCount(project).toInt() + BASE_VERSION_CODE
    } catch (e: Exception) {
        1
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getVersionName(
        project: Project
    ): String = try {
        if (isMaster(project)) {
            "$MAYOR_VERSION.$MINOR_VERSION.${getVersionCode(project) - VERSION_DIF - BASE_VERSION_CODE}"
        } else {
            "0.0.${getVersionCode(project)}" // testing build
        }.also {
            if (isCI()) project.setIOSVersion(it)
        }
    } catch (e: Exception) {
        "0.0.1"
    }

    private fun gitCommitCount(project: Project): String {
        val os = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-list --first-parent --count HEAD".split(" ")
            standardOutput = os
        }
        return String(os.toByteArray()).trim()
    }

    private fun isMaster(project: Project): Boolean {
        val os = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
            standardOutput = os
        }
        return String(os.toByteArray()).trim() == "master"
    }

    private fun isCI() = System.getenv("CI") == "true"

    @Suppress("TooGenericExceptionCaught")
    private fun Project.setIOSVersion(versionName: String) = try {
        exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine = "agvtool new-version -all ${getVersionCode(project)}".split(" ")
        }
        exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine = "agvtool new-marketing-version $versionName".split(" ")
        }
    } catch (e: Exception) {
        println("agvtool exist only mac environment")
        println(e.localizedMessage)
    }
}
