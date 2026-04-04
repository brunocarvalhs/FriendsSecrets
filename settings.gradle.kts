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
include(":core:data")
include(":core:ui")
include(":core:common")
//include(":features:group:list")
include(":features:group:create")
//include(":features:group:details")
//include(":features:group:edit")
//include(":features:group:draw")
include(":features:settings")
