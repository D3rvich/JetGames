plugins {
    alias(libs.plugins.jetgames.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.common"
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.coil.compose)

    implementation(libs.androidx.compose.navigation)

    implementation(libs.kotlinx.serializationJson)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}