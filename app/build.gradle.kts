plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("jacoco") // Add Jacoco plugin for code coverage
}

val cameraxVersion = "1.1.0-alpha01"
android {
    namespace = "com.example.project.waste_recognition_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project.waste_recognition_app"
        minSdk = 23
        targetSdk = 34
        versionCode = 18
        versionName = "1.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        debug {
            // Enable coverage for Android instrumented tests
            enableAndroidTestCoverage = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true // Include Android resources in unit tests
        }
    }
    buildFeatures {
        mlModelBinding = true
        viewBinding = true
    }
}

// Set the Java version for tests
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11)) // Use Java 11 for unit tests
    }
}

// Configure Jacoco for unit test coverage
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest") // Ensure it runs after unit tests

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "/R.class", "/R$.class", "/BuildConfig.", "/Manifest*.*",
        "/Test.", "/*InstrumentedTest.*"
    )

    // Java classes directory
    val javaClasses = fileTree(layout.buildDirectory.dir("intermediates/javac/debug/compileDebugJavaWithJavac/classes")) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(javaClasses)
    sourceDirectories.setFrom(files("src/main/java"))

    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("jacoco/testDebugUnitTest.exec")
    })
}

// Configure Jacoco for Android instrumented tests
tasks.register<JacocoReport>("jacocoAndroidTestReport") {
    dependsOn("connectedDebugAndroidTest") // Ensure it runs after Android instrumentation tests

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "/R.class", "/R$.class", "/BuildConfig.", "/Manifest*.*",
        "/Test.", "/*AndroidTest.*"
    )

    // Java classes directory for instrumentation tests
    val javaClasses = fileTree(layout.buildDirectory.dir("intermediates/javac/debug/compileDebugJavaWithJavac/classes")) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(javaClasses)
    sourceDirectories.setFrom(files("src/main/java"))

    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("outputs/code_coverage/debugAndroidTest/connected//*.ec") // Default location for Android instrumentation coverage
    })
}

// Finalize unit tests by generating the Jacoco report
tasks.withType<Test> {
    finalizedBy(tasks.named("jacocoTestReport")) // Generate the unit test coverage report after tests run
}

// Finalize instrumentation tests by generating the Jacoco report
tasks.withType<Test> {
    finalizedBy(tasks.named("jacocoAndroidTestReport")) // Generate the instrumentation test coverage report after tests run
}

dependencies {
    // Default dependency for JAR files in the libs directory
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-inappmessaging:21.0.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage:20.1.0")

    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.activity:activity:1.9.3")

    // CameraX dependencies
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:1.3.4")

    // Material design components
    implementation("com.google.android.material:material:1.12.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // TensorFlow Lite dependencies
    implementation("org.tensorflow:tensorflow-lite-support:0.3.1")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation("org.tensorflow:tensorflow-lite:2.11.0")

    // ML Kit dependencies
    implementation("com.google.mlkit:image-labeling-custom:16.0.0")
    implementation("com.google.mlkit:linkfirebase:17.0.0")

    // GIF support
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.19")

    // Lottie animations
    implementation("com.airbnb.android:lottie:5.0.3")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("androidx.test:core:1.5.0")


    testImplementation("org.mockito:mockito-inline:4.5.1")
    testImplementation("com.google.firebase:firebase-auth:21.1.0")
    testImplementation("com.google.firebase:firebase-firestore:24.3.0")

    testImplementation("org.robolectric:robolectric:4.8")
    testImplementation("com.google.firebase:firebase-auth:21.1.0")
    testImplementation("com.google.firebase:firebase-firestore:24.3.0")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("org.mockito:mockito-inline:4.5.1")

    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.robolectric:robolectric:4.9")

    // AndroidX Test Core
    testImplementation("androidx.test:core:1.5.0")

    // JUnit for AndroidX Test
    testImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Espresso for UI Testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // ActivityScenarioRule
    androidTestImplementation("androidx.test:rules:1.5.0")

    // Robolectric for Unit Tests
    testImplementation("org.robolectric:robolectric:4.9")


    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:rules:1.5.0")


}