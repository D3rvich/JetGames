plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.feature.impl)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.feature.main.component"
}

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.feature.browse.api)
    implementation(projects.feature.favorites.api)
}
