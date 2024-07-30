import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.d3rvich.jetgames.configureKotlinJvm

/**
 * Created by Ilya Deryabin at 24.06.2024
 */
class JvmLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            configureKotlinJvm()
        }
    }
}