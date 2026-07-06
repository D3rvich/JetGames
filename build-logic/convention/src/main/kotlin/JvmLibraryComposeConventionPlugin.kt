import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import ru.d3rvich.jetgames.configureJvmCompose

class JvmLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jetgames.jvm.library")

            val extensions = extensions.getByType<KotlinJvmProjectExtension>()
            configureJvmCompose(extensions)
        }
    }
}