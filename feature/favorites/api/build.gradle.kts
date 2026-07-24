plugins {
    alias(libs.plugins.jetgames.jvm.library)
    alias(libs.plugins.jetgames.android.feature.api)
}

dependencies {
    implementation(projects.core.entity)

    implementation(libs.androidx.paging.common)
    implementation(libs.kotlinx.coroutines)
}
