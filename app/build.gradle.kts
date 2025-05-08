plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.ramadan.readybackendproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ramadan.readybackendproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.21" // Match Kotlin version
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // Configure JUnit 5 testing
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Network dependencies
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.squareup.moshi)
    implementation(libs.moshi.kotlin)

    implementation (libs.converter.gson)

    // Image loading
    implementation(libs.coil)
    implementation(libs.coil.network)

    // Dependency injection
    implementation(libs.hilt.android)
    androidTestImplementation(libs.androidx.core.testing)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)
    // JSON parsing
    ksp(libs.moshi)

    // Coroutines
    implementation(libs.kotlinx.coroutines)

    // Unit testing
    testImplementation(libs.junit)
    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.engine)
    testImplementation(libs.junit5.params)
    testImplementation(libs.junit.vintage)
    testImplementation(libs.mockk)

    // Additional testing dependencies
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)

    // Instrumented testing

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.ui.test.junit4)
    // Debug implementations
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.material3)
// For Kotlin DSL (build.gradle.kts)
    testImplementation(libs.androidx.core.testing)



    implementation(libs.androidx.navigation.compose)
}