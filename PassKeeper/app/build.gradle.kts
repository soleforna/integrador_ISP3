plugins {
    id("com.android.application")
}

android {
    namespace = "com.rocketteam.passkeeper"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.rocketteam.passkeeper"
        minSdk = 28
        targetSdk = 33
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.f0ris.sweetalert:library:1.5.6") //Para las alertas de Sweet Alert
    implementation ("org.mindrot:jbcrypt:0.4") //para hashing
    implementation ("androidx.biometric:biometric:1.2.0-alpha05") //Para la autenftificación biometrica
}
