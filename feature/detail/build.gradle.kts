plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature)
}

android {
    namespace = "ru.d3rvich.detail"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.collections.immutable)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}