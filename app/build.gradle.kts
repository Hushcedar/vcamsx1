plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace   = "com.axiom.voicepitch"
    compileSdk  = 34
    defaultConfig {
        applicationId = "com.axiom.voicepitch"
        minSdk        = 28
        targetSdk     = 34
        versionCode   = 1
        versionName   = "1.0.0"
    }
    buildTypes {
        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled   = false
            isShrinkResources = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    packaging {
        resources.excludes += setOf(
            "META-INF/*.kotlin_module",
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE*",
            "META-INF/NOTICE*",
            "kotlin/**",
            "**.properties",
            "**.bin"
        )
    }
}
dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    compileOnly("de.robv.android.xposed:api:82:sources")
    implementation("androidx.appcompat:appcompat:1.7.0")
}
