import java.util.Properties

plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.hilt)
}

android {
    namespace = "ru.d3rvich.data"

    defaultConfig {
        buildFeatures.buildConfig = true

        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
            properties.load(rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":database"))
    implementation(project(":remote"))

    implementation(libs.androidx.paging.runtime.ktx)
}