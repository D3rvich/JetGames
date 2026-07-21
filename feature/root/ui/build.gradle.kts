plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature.ui)
}

android {
    namespace = "ru.d3rvich.feature.root.ui"
}

dependencies {
    implementation(projects.feature.root.component)
    implementation(projects.feature.main.component)
    implementation(projects.feature.main.ui)
    implementation(projects.feature.detail.api)
    implementation(projects.feature.detail.ui)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.ui)
    implementation(projects.feature.filter.api)
    implementation(projects.feature.filter.ui)
    implementation(projects.feature.screenshots.api)
    implementation(projects.feature.screenshots.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}