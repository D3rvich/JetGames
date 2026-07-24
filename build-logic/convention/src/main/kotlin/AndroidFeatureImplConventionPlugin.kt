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
class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jetgames.android.koin")
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            dependencies {
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":common"))

                "implementation"(libs.findLibrary("kotlinx-collections-immutable").get())
                "implementation"(libs.findLibrary("decompose-core").get())
                "implementation"(libs.findLibrary("mviKotlin-core").get())
                "implementation"(libs.findLibrary("mviKotlin-main").get())
                "implementation"(libs.findLibrary("mviKotlin-extensions-coroutines").get())
            }
        }
    }
}