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
        val apiKey = properties.getProperty("API_KEY")
            ?: throw IllegalArgumentException("API_KEY wasn't found. Please open local.properties file and add API_KEY=KEY_FROM_RAWG.")
        buildConfigField("String", "API_KEY", "\"${apiKey}\"")
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":datastore"))
    implementation(project(":database"))
    implementation(project(":remote"))

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.runtime.ktx)
}