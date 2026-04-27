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
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.common)
    implementation(projects.feature.home)
    implementation(projects.feature.browse)
    implementation(projects.feature.favorites)
    implementation(projects.feature.filter)
    implementation(projects.feature.detail)
    implementation(projects.feature.screenshots)
    implementation(projects.feature.settings)

    implementation(libs.splashscreen)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.compose.navigation)

    // Coil
    implementation(libs.coil.network)
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