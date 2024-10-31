plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

val cameraxVersion = "1.1.0-alpha01"
android {
    namespace = "com.example.project.waste_recognition_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project.waste_recognition_app"
        minSdk = 22
        targetSdk = 34
        versionCode = 18
        versionName = "1.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.google.firebase:firebase-inappmessaging:21.0.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:1.3.4")
    implementation("androidx.preference:preference:1.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.google.android.material:material:1.12.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Firebase requirements
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")

    // ML Kit for custom image labeling (Updated dependency for custom models)
    implementation("com.google.mlkit:image-labeling-custom:16.0.0")
    implementation("com.google.mlkit:linkfirebase:17.0.0") // Optional for Firebase linking

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.5.0")

    // GIF support
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.17")

    implementation("org.tensorflow:tensorflow-lite-support:0.3.1")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
}