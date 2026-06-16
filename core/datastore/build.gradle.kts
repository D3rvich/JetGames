plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.shared.koin)
}

android {
    namespace = "ru.d3rvich.core.datastore"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.datastore)
    testImplementation(libs.junit)
}