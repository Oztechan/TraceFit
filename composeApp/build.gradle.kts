import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidApplication)
        alias(jetbrainsCompose)
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = ProjectSettings.JAVA_VERSION.toString()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    @Suppress("UnusedPrivateProperty")
    sourceSets {
        val androidMain by getting {
            dependencies {
                libs.android.apply {
                    implementation(composeUi)
                    implementation(composeUiToolingPreview)
                    implementation(activityCompose)
                }
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    ProjectSettings.apply {
        namespace = "com.oztechan.tracefit"
        compileSdk = COMPILE_SDK_VERSION

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets["main"].res.srcDirs("src/androidMain/res")
        sourceSets["main"].resources.srcDirs("src/commonMain/resources")

        defaultConfig {
            applicationId = PROJECT_ID
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
        }

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    dependencies {
        debugImplementation(libs.android.composeUiTooling)
    }
}
