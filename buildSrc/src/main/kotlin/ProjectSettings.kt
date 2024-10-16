import org.gradle.api.JavaVersion
import org.gradle.api.Project
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

    val JAVA_VERSION = JavaVersion.VERSION_21

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun getVersionCode(project: Project) = try {
        gitCommitCount(project).toInt() + BASE_VERSION_CODE
    } catch (e: Throwable) {
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
    } catch (e: Throwable) {
        "0.0.1"
    }

    @Suppress("UnstableApiUsage")
    private fun gitCommitCount(project: Project): String = project.providers.exec {
        commandLine("git rev-list --first-parent --count HEAD".split(" "))
    }.standardOutput.asText.get().trim()

    @Suppress("UnstableApiUsage")
    private fun isMaster(project: Project): Boolean = project.providers.exec {
        commandLine("git rev-parse --abbrev-ref HEAD".split(" "))
    }.standardOutput.asText.get().trim() == "master"

    private fun isCI() = System.getenv("CI") == "true"

    @Suppress("TooGenericExceptionCaught", "UnstableApiUsage")
    private fun Project.setIOSVersion(versionName: String) = try {
        project.providers.exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine("agvtool new-version -all ${getVersionCode(project)}".split(" "))
        }
        project.providers.exec {
            workingDir = File("${project.rootDir}/ios")
            commandLine("agvtool new-marketing-version $versionName".split(" "))
        }
    } catch (e: Throwable) {
        println("agvtool exist only mac environment")
        println(e.message)
    }
}
