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
            libs.compose.apply {
                implementation(runtime)
                implementation(foundation)
                implementation(material)
                implementation(componentsResources)
            }
        }
    }
}
