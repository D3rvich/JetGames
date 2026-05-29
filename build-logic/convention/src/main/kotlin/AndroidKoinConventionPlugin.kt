import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.libs

class AndroidKoinConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jetgames.koin")

            dependencies {
                "implementation"(libs.findLibrary("koin-android").get())
                "implementation"(libs.findLibrary("koin-compose").get())
                "implementation"(libs.findLibrary("koin-compose-viewmodel").get())
            }
        }
    }
}