plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.feature.impl)
}

android {
    namespace = "ru.d3rvich.feature.browse.impl"
}

dependencies {
    implementation(projects.feature.browse.api)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}