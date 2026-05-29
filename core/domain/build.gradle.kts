plugins {
    alias(libs.plugins.jetgames.jvm.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koin.compiler)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.paging.common)

    implementation(platform(libs.koin))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
}