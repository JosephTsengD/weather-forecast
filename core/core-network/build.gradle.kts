import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// 讀取 local.properties 中的 API key
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    FileInputStream(localPropertiesFile).use { localProperties.load(it) }
} else {
    println("WARNING: local.properties file not found!")
}

android {
    namespace = "com.weatherforecast.core.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        
        // 從 local.properties 讀取 API key
        val apiKey = localProperties.getProperty("WEATHER_API_KEY") ?: ""
        println("API Key from local.properties: ${if (apiKey.isEmpty()) "EMPTY" else apiKey.take(4) + "..."}")
        buildConfigField("String", "WEATHER_API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:core-common"))
    
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    
    // Koin
    implementation(libs.koin.android)
}
