import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.configureKotlinAndroid
import ru.d3rvich.jetgames.implementation
import ru.d3rvich.jetgames.libs
import ru.d3rvich.jetgames.testImplementation

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                lint.targetSdk = 35
                testOptions {
                    targetSdk = 35
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                implementation(libs.findLibrary("timber").get())

                testImplementation(libs.findLibrary("junit").get())
            }
        }
    }
}