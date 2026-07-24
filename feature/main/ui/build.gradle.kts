plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature.ui)
}

android {
    namespace = "ru.d3rvich.feature.main.ui"
}

dependencies {
    implementation(projects.feature.main.component)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.ui)
    implementation(projects.feature.browse.api)
    implementation(projects.feature.browse.ui)
    implementation(projects.feature.favorites.api)
    implementation(projects.feature.favorites.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}