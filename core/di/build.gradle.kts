plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.jetgames.android.koin)
}

android {
    namespace = "ru.d3rvich.core.di"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.remote)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.browse.impl)
    implementation(projects.feature.favorites.impl)
    implementation(projects.feature.filter.impl)
    implementation(projects.feature.detail.impl)
    implementation(projects.feature.screenshots.impl)
    implementation(projects.feature.settings.impl)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}