enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

include(
    ":android",
    ":client"
)

rootProject.name = "TraceFit"
rootProject.updateBuildFileNames()

fun ProjectDescriptor.updateBuildFileNames() {
    buildFileName = path
        .drop(1)
        .replace(":", "-")
        .dropLastWhile { it != '-' }
        .plus(name)
        .plus(".gradle.kts")

    if (children.isNotEmpty()) {
        children.forEach { it.updateBuildFileNames() }
    }
}
