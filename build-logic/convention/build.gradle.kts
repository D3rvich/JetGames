import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "ru.d3rvich.jetgames.buildLogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "jetgames.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "jetgames.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "jetgames.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeatureApi") {
            id = "jetgames.android.feature.api"
            implementationClass = "AndroidFeatureApiConventionPlugin"
        }
        register("androidFeatureImpl") {
            id = "jetgames.android.feature.impl"
            implementationClass = "AndroidFeatureImplConventionPlugin"
        }
        register("jvmLibrary") {
            id = "jetgames.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("jvmLibraryCompose") {
            id = "jetgames.jvm.library.compose"
            implementationClass = "JvmLibraryComposeConventionPlugin"
        }
        register("sharedKoin") {
            id = "jetgames.shared.koin"
            implementationClass = "SharedKoinConventionPlugin"
        }
        register("androidKoin") {
            id = "jetgames.android.koin"
            implementationClass = "AndroidKoinConventionPlugin"
        }
    }
}