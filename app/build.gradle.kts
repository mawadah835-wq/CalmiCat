plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.calmicat"

    // DIUBAH KE 36: Agar cocok dengan library activity:1.12.2
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.calmicat"

        // TETAP 23: Agar aplikasi berjalan di Android 6.0 ke atas
        minSdk = 23

        // Boleh tetap 34 atau naik ke 35
        targetSdk = 35

        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
}