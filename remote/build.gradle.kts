import java.util.Properties

plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.network"

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
    implementation(libs.retrofit)
    implementation(libs.retrofit.loggingInterceptor)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.kotlinx.datetime)
    implementation(libs.retrofit.kotlinSerailization)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.logging.slf4j)

    testImplementation(libs.junit)
}