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
        maven { url = uri("https://jitpack.io") }
        // IjkPlayer mirror — Aliyun was down, using direct Maven Central + jitpack fallback
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://repo1.maven.org/maven2/") }
    }
}
rootProject.name = "vcamsx"
include(":app")
