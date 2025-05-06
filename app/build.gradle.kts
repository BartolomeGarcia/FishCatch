plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fishcatch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fishcatch"
        minSdk = 30
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.gms:play-services-location:21.0.1")  //Para Ubicación
    implementation("com.squareup.retrofit2:retrofit:2.9.0") //Para Temperatura
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")   //Para Temperatura
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    testImplementation(libs.junit)
}
