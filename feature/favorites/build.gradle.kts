plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.jetgames.android.feature)
}

android {
    namespace = "ru.d3rvich.favorites"
}

dependencies {
    implementation(libs.coil.compose)

    implementation(libs.androidx.paging.compose)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}