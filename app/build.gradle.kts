plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.google.firebase.firebase.perf)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    id("org.jetbrains.dokka") version "1.9.20"
    kotlin("plugin.serialization") version "2.1.10"
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
}

android {
    namespace = "br.com.brunocarvalhs.friendssecrets"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.brunocarvalhs.friendssecrets"
        minSdk = 24
        targetSdk = 35
        
        versionCode = 12
        versionName = "2.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("${rootDir}/app/release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEYSTORE_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    lint {
        disable += "NullSafeMutableLiveData"
        baseline = file("lint-baseline.xml")
    }
}

detekt {
    config.from(files("$rootDir/detekt.yml"))
}

dependencies {
    implementation(project(":features:group:list"))
    implementation(project(":features:group:details"))
    implementation(project(":features:group:create"))
    implementation(project(":features:group:draw"))
    implementation(project(":features:settings"))
    implementation(project(":features:chat"))

    implementation("androidx.startup:startup-runtime:1.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.timber)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.hilt.android)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.process)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    androidTestImplementation (libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)

    implementation(libs.kotlinx.serialization.json)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.perf)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
}
