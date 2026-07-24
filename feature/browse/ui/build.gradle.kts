plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature.ui)
}

android {
    namespace = "ru.d3rvich.feature.browse.ui"
}

dependencies {
    implementation(projects.feature.browse.api)

    implementation(libs.coil.network)
    implementation(libs.coil.compose)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}