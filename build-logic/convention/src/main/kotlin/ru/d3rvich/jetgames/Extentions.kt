package ru.d3rvich.jetgames

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

/**
 * Created by Ilya Deryabin at 11.05.2024
 */
val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    add("implementation", dependencyNotation)
}

internal fun DependencyHandlerScope.testImplementation(dependencyNotation: Any) {
    add("testImplementation", dependencyNotation)
}

internal fun DependencyHandlerScope.androidTestImplementation(dependencyNotation: Any) {
    add("androidTestImplementation", dependencyNotation)
}

internal fun DependencyHandlerScope.debugImplementation(dependencyNotation: Any) {
    add("debugImplementation", dependencyNotation)
}

internal fun DependencyHandlerScope.ksp(dependencyNotation: Any) {
    add("ksp", dependencyNotation)
}