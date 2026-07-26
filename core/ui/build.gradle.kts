plugins {
    alias(libs.plugins.jetgames.android.library.compose)
}

android {
    namespace = "ru.d3rvich.core.ui"
}

dependencies {
    api(projects.core.ui.model)
    implementation(projects.core.domain)
    implementation(projects.common)

    implementation(libs.decompose.core)
    implementation(libs.mviKotlin.core)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)

    implementation(libs.coil.compose)

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.paging.compose)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}