import io.gitlab.arturbosch.detekt.Detekt

plugins {
    libs.plugins.apply {
        alias(jetbrainsCompose).apply(false)
        alias(androidApplication).apply(false)
        alias(kotlinMultiplatform).apply(false)
        alias(kotlinAndroid).apply(false)
        alias(androidLibrary).apply(false)
        alias(kover)
        alias(detekt)
    }
}

allprojects {
    apply(plugin = rootProject.libs.plugins.kover.get().pluginId).also {
        rootProject.dependencies.add("kover", project(path))
        kover.reports.filters.excludes.annotatedBy(
            "androidx.compose.ui.tooling.preview.Preview",
            "androidx.compose.runtime.Composable"
        )
    }

    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId).also {
        detekt {
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
            config.from(rootProject.layout.projectDirectory.file("detekt.yml"))
        }

        tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
            // Use providers to avoid direct project references
            val projectDirectory = layout.projectDirectory.asFile
            val buildDirectory = layout.buildDirectory.asFile

            setSource(projectDirectory)
            exclude("**/build/**")
            exclude {
                val relativePath = it.file.relativeTo(projectDirectory)
                relativePath.startsWith(buildDirectory.get().relativeTo(projectDirectory))
            }
        }

        tasks.register("detektAll") {
            dependsOn(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>())
        }

        dependencies {
            detektPlugins(rootProject.libs.common.detektFormatting)
        }
    }
}
