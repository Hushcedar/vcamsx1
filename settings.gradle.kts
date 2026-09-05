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
        // IjkPlayer + PickerView — only available on this Aliyun endpoint
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        // Fallback direct Maven Central
        maven { url = uri("https://repo1.maven.org/maven2/") }
    }
}
rootProject.name = "vcamsx"
include(":app")
