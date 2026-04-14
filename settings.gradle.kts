pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JetGames"
include(":app")
include(":core:domain")
include(":core:ui")
include(":common")
include(":data")
include(":remote")
include(":database")
include(":feature:home")
include(":feature:filter")
include(":feature:detail")
include(":feature:screenshots")
include(":feature:favorites")
include(":feature:browse")
include(":feature:settings")
include(":datastore")
