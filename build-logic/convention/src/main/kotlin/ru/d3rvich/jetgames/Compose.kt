package ru.d3rvich.jetgames

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    commonExtension.apply {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        buildFeatures.apply {
            compose = true
        }
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "implementation"(libs.findLibrary("androidx-compose-ui-core").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
            "implementation"(libs.findLibrary("androidx-compose-material3").get())
            "implementation"(libs.findLibrary("androidx-compose-material3-windowSizeClass").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-preview").get())
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
    configureCompose<KotlinAndroidProjectExtension>()
}

internal fun Project.configureJvmCompose(kotlinJvmExtension: KotlinJvmProjectExtension) {
    kotlinJvmExtension.apply {
        dependencies {
            "implementation"(libs.findLibrary("androidx-compose-runtime").get())
        }
    }
    configureCompose<KotlinJvmProjectExtension>()
}

private inline fun <reified T : KotlinBaseExtension> Project.configureCompose() = configure<T> {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    val enableMetrics = project.providers.gradleProperty("enableComposeCompilerReports")
        .map(String::toBoolean).getOrElse(false)

    pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") {
        configure<ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose_compiler_config.conf"))

            if (enableMetrics) {
                metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
                reportsDestination.set(layout.buildDirectory.dir("compose_reports"))
            }
        }
    }
}