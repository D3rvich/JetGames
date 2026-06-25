plugins {
    alias(libs.plugins.jetgames.jvm.library)
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.decompose.core)
    implementation(libs.androidx.compose.runtime)
}