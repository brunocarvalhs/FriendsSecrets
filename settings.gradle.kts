pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "FriendsSecrets"
include(":app")
include(":core:domain")
include(":core:network")
include(":core:remote")
include(":core:security")
include(":core:navigation")
include(":core:ui")
include(":core:analytics")
include(":core:logger")
include(":features:group:list")
include(":features:group:create")
include(":features:group:details")
include(":features:group:draw")
include(":features:settings")
include(":features:biometric")
include(":features:chat")
include(":core:biometric")
include(":core:storage")
include(":core:deviceid")
include(":core:notifications")
include(":baselineprofile")
