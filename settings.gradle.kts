enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.enterprise") version ("3.16.2")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()

        obfuscation {
            username { null }
            hostname { null }
            ipAddresses { null }
        }
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
