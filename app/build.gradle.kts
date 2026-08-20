plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vcam.app"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.vcam.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        ndk { abiFilters += listOf("arm64-v8a") }
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("${rootProject.projectDir}/keystore/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildTypes {
        debug   { signingConfig = signingConfigs.getByName("debug") }
        release { signingConfig = signingConfigs.getByName("debug"); isMinifyEnabled = false }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
    buildFeatures { viewBinding = true }
    packagingOptions {
        jniLibs { excludes += listOf("**/armeabi-v7a/**","**/x86/**","**/x86_64/**") }
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.code.gson:gson:2.10.1")

    // Xposed — correct repo
    compileOnly("de.robv.android.xposed:api:82:sources")
    compileOnly("de.robv.android.xposed:api:82")

    // IjkPlayer — from Aliyun mirror (official repo is dead, this works)
    implementation("tv.danmaku.ijk.media:ijkplayer-java:0.8.8")
    implementation("tv.danmaku.ijk.media:ijkplayer-arm64:0.8.8")
}
