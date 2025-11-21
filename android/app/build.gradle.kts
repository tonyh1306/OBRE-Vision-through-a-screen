plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "edu.vassar.cmpu203.obre"
    compileSdk = 36

    defaultConfig {
        applicationId = "edu.vassar.cmpu203.obre"
        minSdk = 32
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {

        getByName("main") {
            // This points to: android/opencv/jniLibs

            jniLibs.srcDirs(
                "src/main/jniLibs",
                project(":opencv").file("native/libs"),
                project(":opencv").file("jniLibs")
            )
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(project(":opencv"))
    implementation(libs.fragment)
    implementation(libs.recyclerview)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    val cameraxVersion = "1.3.3"

    // --- CAMERA X LIBRARIES (Java-compatible) ---
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")

    // THIS one gives you PreviewView
    implementation("androidx.camera:camera-view:$cameraxVersion")

    // Optional (if you later do ML/YOLO/OpenCV)
    implementation("androidx.camera:camera-extensions:$cameraxVersion")

    // --- OTHER ANDROIDX JAVA LIBRARIES ---
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    val composeBom = platform("androidx.compose:compose-bom:2025.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material Design 3
    implementation("androidx.compose.material3:material3")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Add window size utils
    implementation("androidx.compose.material3.adaptive:adaptive")

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.11.0")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")

    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-firestore:25.1.3")

    implementation("com.google.guava:guava:31.1-android")

    // Source - https://stackoverflow.com/a
    // Posted by Dmitri Chernysh
    // Retrieved 2025-11-20, License - CC BY-SA 4.0
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.16.0-rc1")
}
