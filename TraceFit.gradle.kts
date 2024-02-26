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

    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId).also {
        detekt {
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
            config.from("${rootProject.projectDir}/detekt.yml")
        }
        tasks.withType<Detekt> {
            setSource(files(project.projectDir))
            exclude("**/build/**")
            exclude {
                it.file.relativeTo(projectDir).startsWith(
                    project.layout.buildDirectory.asFile.get().relativeTo(projectDir)
                )
            }
        }.onEach { detekt ->
            // skip detekt tasks unless a it is specifically called
            detekt.onlyIf {
                gradle.startParameter.taskNames.any { it.contains("detekt") }
            }
        }

        tasks.register("detektAll") {
            dependsOn(tasks.withType<Detekt>())
        }

        dependencies {
            detektPlugins(rootProject.libs.common.detektFormatting)
        }
    }
}
