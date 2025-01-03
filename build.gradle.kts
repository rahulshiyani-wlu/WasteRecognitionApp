buildscript {
    val agpVersion by extra("8.6.1")
    val agp_version by extra("8.7.2")
    val agp_version1 by extra("8.7.2")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:perf-plugin:1.4.2") // Update or remove if it causes issues firebase plugin
        classpath("com.android.tools.build:gradle:8.6.1")
        classpath("com.google.gms:google-services:4.3.15")

    // app dependencies is in another file
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}