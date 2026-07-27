import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(jetbrainsCompose)
        alias(androidKotlinMultiplatformLibrary)
        alias(kotlinPluginCompose)
    }
}

kotlin {
    android {
        ProjectSettings.apply {
            namespace = "$PROJECT_ID.client"
            compileSdk = COMPILE_SDK_VERSION
            minSdk = MIN_SDK_VERSION

            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(JAVA_VERSION.toString()))
                    }
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Client"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }
    }
}
