plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.aspectj.android)
}

android {
    namespace = "br.com.brunocarvalhs.friendssecrets.core.analytics"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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

afterEvaluate {
    val androidComponents =
        extensions.getByType<com.android.build.api.variant.LibraryAndroidComponentsExtension>()
    val bootClasspathProvider = androidComponents.sdkComponents.bootClasspath
    tasks.withType<io.freefair.gradle.plugins.aspectj.AspectjCompile>().configureEach {
        ajcOptions.bootclasspath.from(bootClasspathProvider)
    }
}

detekt {
    config.from(files("$rootDir/detekt.yml"))
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.aspectjrt)
    implementation(libs.timber)
}
