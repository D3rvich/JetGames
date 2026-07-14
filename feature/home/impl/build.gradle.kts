plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.feature.impl)
}

android {
    namespace = "ru.d3rvich.feature.home.impl"
}

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.core.domain)

    implementation(libs.androidx.paging.common)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}