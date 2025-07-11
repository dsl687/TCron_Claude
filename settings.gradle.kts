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
        maven("https://jitpack.io")
    }
}

rootProject.name = "TCron"

include(":app")

// Core modules
include(":core:common")
include(":core:data")
include(":core:domain")

// Feature modules
include(":feature:home")
include(":feature:terminal")
include(":feature:task")
include(":feature:dashboard")
include(":feature:settings")
include(":feature:notification")