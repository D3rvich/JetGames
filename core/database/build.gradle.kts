plugins {
    alias(libs.plugins.jetgames.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.d3rvich.database"
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.paging.runtime.ktx)

    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.serializationJson)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.datetime)
}