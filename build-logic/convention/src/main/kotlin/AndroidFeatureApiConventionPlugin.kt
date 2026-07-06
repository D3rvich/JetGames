import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import ru.d3rvich.jetgames.libs

class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jetgames.jvm.library.compose")
            dependencies {
                "implementation"(libs.findLibrary("decompose-core").get())
            }
        }
    }
}