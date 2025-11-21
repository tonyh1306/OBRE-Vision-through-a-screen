import java.util.Properties
import java.io.FileInputStream

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

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(FileInputStream(localPropertiesFile))
        }

        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY")}\"")
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
<<<<<<< HEAD

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

=======
    buildFeatures {
        buildConfig = true
    }
>>>>>>> LLMAlgo
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(project(":opencv"))
<<<<<<< HEAD
    implementation(libs.fragment)
    implementation(libs.recyclerview)
    implementation(libs.firebase.crashlytics.buildtools)
=======
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.google.guava:guava:33.0.0-android")
    implementation(libs.firebase.inappmessaging)
    implementation(libs.common)
    implementation(libs.firebase.ai)
    implementation("com.squareup.okhttp3:okhttp:4.11.0")


    // Required Ktor clients for Android
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-okhttp:2.3.7")
    implementation("io.ktor:ktor-client-serialization:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    implementation(libs.generativeai)

    // Required for instrumented tests
    androidTestImplementation("io.ktor:ktor-client-core:2.3.7")
    androidTestImplementation("io.ktor:ktor-client-okhttp:2.3.7")
    androidTestImplementation("io.ktor:ktor-client-serialization:2.3.7")
    androidTestImplementation("io.ktor:ktor-client-logging:2.3.7")
    testImplementation("junit:junit:4.13.2")

>>>>>>> LLMAlgo
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

    // Optional - Add window size utils
    implementation("androidx.compose.material3.adaptive:adaptive")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.11.0")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")

    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-firestore:25.1.3")

    implementation("com.google.guava:guava:31.1-android")
}