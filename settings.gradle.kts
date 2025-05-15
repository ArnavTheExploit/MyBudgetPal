pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Add this JetBrains Compose repo for Compose dependencies
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  // This ensures no project-level repositories are used
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyBudgetPal"
include(":app")
