plugins {
    alias(libs.plugins.jetgames.android.application.compose)
    alias(libs.plugins.jetgames.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.jetgames"

    defaultConfig {
        applicationId = "ru.d3rvich.jetgames"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(project(":feature:home"))
    implementation(project(":feature:browse"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:filter"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:screenshots"))

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.compose.navigation)

    // Coil
    implementation(libs.coil.compose)

    // Hilt additional
    implementation(libs.androidx.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines)

    implementation(libs.kotlinx.serializationJson)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
}