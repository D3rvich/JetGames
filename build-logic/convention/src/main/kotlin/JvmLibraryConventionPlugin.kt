import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.d3rvich.jetgames.configureKotlinJvm
import org.gradle.kotlin.dsl.apply

/**
 * Created by Ilya Deryabin at 24.06.2024
 */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")

            configureKotlinJvm()
        }
    }
}