plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.hilt)
    alias(libs.plugins.koin.compiler)
}

android {
    namespace = "ru.d3rvich.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.remote)

    implementation(platform(libs.koin))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.runtime.ktx)
}