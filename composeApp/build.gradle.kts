plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(compose)
        alias(androidApplication)
    }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        val commonMain by getting {
            dependencies {
                compose.apply {
                    implementation(runtime)
                    implementation(material3)
                    implementation(materialIconsExtended)
                    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                    implementation(components.resources)
                }
            }
        }


        val androidMain by getting {
            dependencies {
                libs.android.apply {
                    implementation(composeActivity)
                    implementation(composeToolingPreview)
                    implementation(appcompat)
                }
            }
        }
    }
}

android {
    namespace = "com.oztechan.tracefit"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "com.oztechan.tracefit.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
        resources.srcDirs("src/commonMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

