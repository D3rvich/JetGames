plugins {
    alias(libs.plugins.jetgames.jvm.library)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.paging.common)
}