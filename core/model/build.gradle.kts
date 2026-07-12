plugins {
    alias(libs.plugins.jetgames.jvm.library)
}

dependencies {
    implementation(projects.core.entity)

    implementation(libs.kotlinx.coroutines)
}
