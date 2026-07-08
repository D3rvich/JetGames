import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.libs

class AndroidFeatureUiConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jetgames.android.library")
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
                "implementation"(libs.findLibrary("decompose-extensionsCompose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
            }
        }
    }
}