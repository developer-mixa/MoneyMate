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
    }
    val okHttpVersion = "4.9.3"
    val retrofitVersion = "2.9.0"

    versionCatalogs {
        create("libs") {

            library("android.coreKtx", "androidx.core:core-ktx:1.9.0")
            library("android.appCompat", "androidx.appcompat:appcompat:1.6.1")
            library("android.constraintLayout", "androidx.constraintlayout:constraintlayout:2.1.4")
            library("android.activityKtx", "androidx.activity:activity-ktx:1.6.1")
            library("android.lifecycleRuntimeKtx", "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
            library("android.lifecycleViewModelKtx", "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

            library("backend.okHttp", "com.squareup.okhttp3:okhttp:$okHttpVersion")
            library("backend.okHttpInterceptor", "com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
            library("backend.retrofit", "com.squareup.retrofit2:retrofit:$retrofitVersion")
            library("backend.moshi", "com.squareup.retrofit2:converter-moshi:$retrofitVersion")

            library("google.material", "com.google.android.material:material:1.12.0")

            library("test.junit", "junit:junit:4.13.2")
            library("test.mock", "io.mockk:mockk:1.12.4")
            library("test.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
        }
    }
}

rootProject.name = "MoneyMate"
include(":app")
