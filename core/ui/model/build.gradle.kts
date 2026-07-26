plugins {
    alias(libs.plugins.jetgames.jvm.library)
}
dependencies {
    implementation(projects.core.entity)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.compose.runtime)
}
