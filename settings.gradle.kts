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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:di")
include(":core:domain")
include(":core:entity")
include(":core:model")
include(":core:navigation")
include(":core:remote")
include(":core:ui")
include(":feature:browse")
include(":feature:detail")
include(":feature:favorites")
include(":feature:filter:api")
include(":feature:filter:impl")
include(":feature:filter:ui")
include(":feature:home")
include(":feature:screenshots:api")
include(":feature:screenshots:impl")
include(":feature:screenshots:ui")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:settings:ui")
