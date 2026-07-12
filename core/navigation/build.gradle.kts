import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetgames.android.koin)
}

configure<LibraryExtension> {
    namespace = "ru.d3rvich.core.navigation"
}

dependencies {
    implementation(projects.common)
    implementation(projects.core.domain)
    implementation(projects.core.di)
    implementation(projects.feature.home)
    implementation(projects.feature.browse)
    implementation(projects.feature.favorites)
    implementation(projects.feature.detail)
    implementation(projects.feature.screenshots.api)
    implementation(projects.feature.screenshots.impl)
    implementation(projects.feature.screenshots.ui)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)
    implementation(projects.feature.settings.ui)
    implementation(projects.feature.filter.api)
    implementation(projects.feature.filter.impl)
    implementation(projects.feature.filter.ui)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.decompose.core)
    implementation(libs.decompose.extensionsCompose)
    implementation(libs.decompose.jetpackComponentContext)
}
