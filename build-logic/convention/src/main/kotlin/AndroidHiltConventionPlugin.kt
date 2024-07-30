import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.implementation
import ru.d3rvich.jetgames.ksp
import ru.d3rvich.jetgames.libs

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
class AndroidHiltConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }
            dependencies {
                implementation(libs.findLibrary("androidx-hilt-android").get())
                ksp(libs.findLibrary("androidx-hilt-compiler").get())
            }
        }
    }
}