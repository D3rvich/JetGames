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
    implementation(projects.feature.screenshots)
    implementation(projects.feature.settings)
    implementation(projects.feature.filter)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.decompose.core)
    implementation(libs.decompose.extentionsCompose)
    implementation(libs.decompose.jetpackComponentContext)
}
