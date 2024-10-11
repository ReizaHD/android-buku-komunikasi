plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.bintangjuara.bk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bintangjuara.bk"
        minSdk = 21
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
    implementation(libs.swiperefreshlayout)
    implementation(libs.gson)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation (libs.java.websocket)
    implementation(libs.volley)
    implementation(libs.glide)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    annotationProcessor(libs.compiler)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}