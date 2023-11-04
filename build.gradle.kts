plugins {
    libs.plugins.apply {
        alias(jetbrainsCompose) apply false
        alias(androidApplication) apply false
        alias(kotlinMultiplatform) apply false
        alias(kover)
    }
}

// todo CMP bug needs to be removed when fixed
// https://youtrack.jetbrains.com/issue/KT-41821/Kotlin-1.4.10-ios-target-java.lang.IllegalStateException-failed-to-resolve-Kotlin-library-org.jetbrains.kotlinx-atomicfu-common
buildscript {
    dependencies {
        classpath(libs.classpaths.atomicfu)
    }
}

allprojects {
    apply(plugin = rootProject.libs.plugins.kover.get().pluginId).also {
        koverMerged {
            filters {
                annotations {
                    excludes += listOf(
                        "androidx.compose.ui.tooling.preview.Preview",
                        "androidx.compose.runtime.Composable"
                    )
                }
            }
            enable()
        }
    }

    // todo CMP bug needs to be removed when fixed
    // https://youtrack.jetbrains.com/issue/KT-41821/Kotlin-1.4.10-ios-target-java.lang.IllegalStateException-failed-to-resolve-Kotlin-library-org.jetbrains.kotlinx-atomicfu-common
    apply(plugin = rootProject.libs.plugins.atomicfu.get().pluginId)
}
