plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature.ui)
}

android {
    namespace = "ru.d3rvich.feature.screenshots.ui"
}

dependencies {
    implementation(projects.feature.screenshots.api)

    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}