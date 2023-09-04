plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("kotlin-parcelize")



}

android {
    namespace = "com.example.totopartnetapppracticeapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.totopartnetapppracticeapplication"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    /*Dimen lib*/
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")
    //circle imageview
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    /*Lottie Animation*/
    implementation  ("com.airbnb.android:lottie:5.2.0")
    /*Google Maps SDK*/
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.maps.android:android-maps-utils:1.3.1")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    //koin
    // Koin Core features
    implementation ("io.insert-koin:koin-core:3.2.1")
    // Koin main features for Android
    implementation ("io.insert-koin:koin-android:3.2.1")
    //retrofit
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")

    //coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //Add KTX dependencies
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    /*Room Database*/
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-rxjava2:2.5.2")
    //coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    //Add KTX dependencies
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    ///socket.io
//    implementation ("com.github.nkzawa:socket.io-client:0.5.2")
    /*Socket IO*/
//    implementation ('io.socket:socket.io-client:1.0.0') {
//        exclude group: 'org.json', module: 'json'    }
//    implementation ("io.socket:socket.io-client:2.0.0") {
//        exclude group: 'org.json', module: 'json'
//    }
     implementation ("io.socket:socket.io-client:1.0.0")

}
//kapt {
//    correctErrorTypes = true
//}