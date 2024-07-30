plugins {
    alias(libs.plugins.jetgames.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.loggingInterceptor)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.retrofit.kotlinSerailization)

    testImplementation(libs.junit)
}