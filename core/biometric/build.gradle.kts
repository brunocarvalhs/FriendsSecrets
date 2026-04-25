plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.detekt)
}

android {
    namespace = "br.com.brunocarvalhs.core.biometric"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

detekt {
    config.from(files("$rootDir/detekt.yml"))
}

dependencies {
    implementation(project(":core:storage"))
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.process)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.biometric)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}