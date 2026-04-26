plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.hilt)
}

android {
    namespace = "ru.d3rvich.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.remote)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.runtime.ktx)
}