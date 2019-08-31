import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {

    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "io.github.rsookram.notes"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 1
        versionName = "1.0"

        resConfigs("en")

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")

            // Just for testing release builds. Not actually distributed.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    packagingOptions {
        exclude("/kotlin/**")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.allWarningsAsErrors = true
}

dependencies {
    implementation(Versions.appCompat)
    implementation(Versions.materialComponents)
    implementation(Versions.coreKtx)
    implementation(Versions.activityKtx)
    implementation(Versions.lifecycleViewModelKtx)

    implementation(Versions.inboxRecyclerView)

    implementation(Versions.epoxyCore)
    kapt(Versions.epoxyProcessor)

    implementation(Versions.roomRuntime)
    implementation(Versions.roomKtx)
    kapt(Versions.roomCompiler)

    implementation(Versions.kotlinStdlib)
    implementation(Versions.kotlinCoroutinesCore)
    implementation(Versions.kotlinCoroutinesAndroid)
}

kapt {
    // Needed for epoxy's annotation processor
    correctErrorTypes = true
}
