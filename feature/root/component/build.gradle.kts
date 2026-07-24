plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.feature.impl)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.feature.root.component"
}

dependencies {
    implementation(projects.feature.main.component)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.filter.api)
    implementation(projects.feature.detail.api)
    implementation(projects.feature.screenshots.api)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}