plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.hilt)
}

android {
    namespace = "ru.d3rvich.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":datastore"))
    implementation(project(":database"))
    implementation(project(":remote"))

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.paging.runtime.ktx)
}