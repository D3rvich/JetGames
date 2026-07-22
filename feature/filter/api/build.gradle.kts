plugins {
    alias(libs.plugins.jetgames.jvm.library)
    alias(libs.plugins.jetgames.android.feature.api)
}

dependencies {
    implementation(projects.core.entity)
    implementation(projects.core.model)

    implementation(libs.kotlinx.collections.immutable)
}
