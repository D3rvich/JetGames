plugins {
    alias(libs.plugins.jetgames.android.application.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetgames.android.koin)
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
    implementation(projects.core.di)
    implementation(projects.feature.root.component)
    implementation(projects.feature.root.ui)

    implementation(libs.splashscreen)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // Decompose
    implementation(libs.decompose.core)

    // Coil
    implementation(libs.coil)

    // Coroutines
    implementation(libs.kotlinx.coroutines)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
}