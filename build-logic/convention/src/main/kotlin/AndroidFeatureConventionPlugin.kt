import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.implementation
import ru.d3rvich.jetgames.libs

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jetgames.android.library")
                apply("jetgames.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            dependencies {
                implementation(project(":core:domain"))
                implementation(project(":core:ui"))
                implementation(project(":common"))

                implementation(libs.findLibrary("androidx-hilt-navigation-compose").get())
                implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                implementation(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
            }
        }
    }
}