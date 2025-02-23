plugins {
    alias(libs.plugins.jetgames.android.library.compose)
}

android {
    namespace = "ru.d3rvich.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":common"))

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.coil.network)
    implementation(libs.coil.compose)

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}