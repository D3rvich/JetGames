import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import ru.d3rvich.jetgames.configureAndroidCompose
import ru.d3rvich.jetgames.configureKotlinAndroid
import ru.d3rvich.jetgames.implementation
import ru.d3rvich.jetgames.libs
import ru.d3rvich.jetgames.testImplementation

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig {
                    targetSdk = 34
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
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            dependencies {
                implementation(libs.findLibrary("timber").get())
                testImplementation(libs.findLibrary("junit").get())
            }
        }
    }
}