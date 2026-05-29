import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.libs

class KoinConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.insert-koin.compiler.plugin")

            dependencies {
                "implementation"(platform(libs.findLibrary("koin").get()))
                "implementation"(libs.findLibrary("koin-core").get())
                "implementation"(libs.findLibrary("koin-annotations").get())
            }
        }
    }
}