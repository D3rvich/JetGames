plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.feature.impl)
    alias(libs.plugins.jetgames.shared.koin)
}

android {
    namespace = "ru.d3rvich.feature.settings.impl"
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(libs.decompose.core)
    implementation(libs.decompose.extensionsCompose)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}