plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature)
    alias(libs.plugins.jetgames.shared.koin)
}

android {
    namespace = "ru.d3rvich.feature.settings.impl"
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(libs.decompose.core)
    implementation(libs.decompose.extentionsCompose)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}