pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Weather Forecast"
include(":app")
include(":core:core-common")
include(":core:core-network")
include(":core:core-database")
include(":core:core-utils")
include(":feature:feature-weather")
include(":feature:feature-city")
include(":data:data-weather")
