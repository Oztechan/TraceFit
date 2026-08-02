plugins {
    libs.plugins.apply {
        alias(androidApplication)
        alias(jetbrainsCompose)
        alias(kotlinPluginCompose)
    }
}

android {
    ProjectSettings.apply {
        namespace = "$PROJECT_ID.android"
        compileSdk = COMPILE_SDK_VERSION

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
}

dependencies {
    implementation(project(":client"))

    libs.compose.apply {
        implementation(ui)
        implementation(uiToolingPreview)
        debugImplementation(uiTooling)
    }
    libs.android.apply {
        implementation(activityCompose)
    }
}
