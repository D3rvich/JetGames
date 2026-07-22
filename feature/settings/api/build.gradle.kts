plugins {
    alias(libs.plugins.jetgames.jvm.library)
    alias(libs.plugins.jetgames.android.feature.api)
}

dependencies {
    implementation(projects.core.model)
}