plugins {
    alias(libs.plugins.jetgames.android.library)
}

android {
    namespace = "ru.d3rvich.datastore"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.datastore)
    testImplementation(libs.junit)
}