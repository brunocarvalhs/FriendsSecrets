plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
}

android {
    namespace = "br.com.brunocarvalhs.friendssecrets.core.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:security"))
    
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.firebase.config)
    implementation(libs.firebase.perf)
    
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}