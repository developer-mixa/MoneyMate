plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

fun getLocalPropertyKey(key: String): String {
    return com.android.build.gradle.internal.cxx.configure
        .gradleLocalProperties(rootDir).getProperty(key) ?: ""
}

android {
    namespace = "com.example.moneymate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moneymate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val exchangeRatesKey = getLocalPropertyKey("exchange_rates_key")

        buildConfigField("String", "EXCHANGE_RATES_KEY", "\"$exchangeRatesKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.android.coreKtx)
    implementation(libs.android.appCompat)
    implementation(libs.android.constraintLayout)
    implementation(libs.android.activityKtx)
    implementation(libs.android.lifecycleRuntimeKtx)
    implementation(libs.android.lifecycleViewModelKtx)

    implementation(libs.backend.okHttp)
    implementation(libs.backend.okHttpInterceptor)
    implementation(libs.backend.moshi)
    implementation(libs.backend.retrofit)

    implementation(libs.google.material)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mock)
    testImplementation(libs.test.coroutines)
}