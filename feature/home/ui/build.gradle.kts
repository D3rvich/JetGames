plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature.ui)
}

android {
    namespace = "ru.d3rvich.feature.home.ui"
}

dependencies {
    implementation(projects.feature.home.api)

    implementation(libs.coil.compose)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.compose)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}