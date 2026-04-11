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
include(":features:group:list")
include(":features:group:create")
include(":features:group:details")
include(":features:group:draw")
include(":features:settings")
include(":features:biometric")
include(":features:chat")
