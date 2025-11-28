plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    // ▼▼▼ अब यह प्लगइन सही से काम करेगा ▼▼▼
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.raktindia"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.raktindia"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    /*
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    */

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

    dependencies {
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.12.0")


        implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-database")

        implementation("com.google.android.gms:play-services-tasks:18.2.0")
        implementation(libs.google.firebase.firestore)

        val composeBom = platform(libs.androidx.compose.bom)
        implementation(composeBom)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)

        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
    }


