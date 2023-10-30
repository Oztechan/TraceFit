plugins {
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.android.application).apply(false)
}

buildscript {
    dependencies {
        // Use the same version in the error
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.17.3")
    }
}

allprojects {
    apply(plugin = "kotlinx-atomicfu")
}
