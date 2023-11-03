plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform).apply(false)
        alias(compose).apply(false)
        alias(androidApplication).apply(false)
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
    apply(plugin = rootProject.libs.plugins.atomicfu.get().pluginId)
}
