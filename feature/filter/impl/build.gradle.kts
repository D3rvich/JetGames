plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature.impl)
}

android {
    namespace = "ru.d3rvich.feature.filter.impl"
}

dependencies {
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}