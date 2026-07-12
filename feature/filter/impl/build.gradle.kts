plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.feature.impl)
}

android {
    namespace = "ru.d3rvich.feature.filter.impl"
}

dependencies {
    implementation(projects.feature.filter.api)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}