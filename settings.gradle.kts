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
include(":feature:browse:api")
include(":feature:browse:impl")
include(":feature:browse:ui")
include(":feature:detail:api")
include(":feature:detail:impl")
include(":feature:detail:ui")
include(":feature:favorites:api")
include(":feature:favorites:impl")
include(":feature:favorites:ui")
include(":feature:filter:api")
include(":feature:filter:impl")
include(":feature:filter:ui")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:home:ui")
include(":feature:main:component")
include(":feature:main:ui")
include(":feature:root:component")
include(":feature:root:ui")
include(":feature:screenshots:api")
include(":feature:screenshots:impl")
include(":feature:screenshots:ui")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:settings:ui")
