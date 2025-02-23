plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature)
}

android {
    namespace = "ru.d3rvich.browse"
}

dependencies {
    implementation(libs.coil.network)
    implementation(libs.coil.compose)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}