plugins {
    alias(libs.plugins.jetgames.android.library.compose)
}

android {
    namespace = "ru.d3rvich.navigation"
}

dependencies {
    implementation(projects.common)
    api(libs.androidx.navigation3.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}