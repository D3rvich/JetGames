import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.libs

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jetgames.android.library")
            apply(plugin = "jetgames.android.hilt")
            apply(plugin = "io.insert-koin.compiler.plugin")
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":common"))

                "implementation"(platform(libs.findLibrary("koin").get()))
                "implementation"(libs.findLibrary("koin-android").get())
                "implementation"(libs.findLibrary("koin-compose").get())
                "implementation"(libs.findLibrary("koin-compose-viewmodel").get())
                "implementation"(libs.findLibrary("koin-annotations").get())
                "implementation"(libs.findLibrary("androidx-hilt-navigation-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
            }
        }
    }
}