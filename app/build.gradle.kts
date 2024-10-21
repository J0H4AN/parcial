plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")
}

android {
    namespace = "com.example.biblioteca"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.biblioteca"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.room:room-runtime:2.5.0") // Reemplaza con la versión que necesites
    kapt("androidx.room:room-compiler:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0") // Opcional, para corutina

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // Asegúrate de que el BOM está incluido
    implementation(libs.androidx.ui) // Esto es correcto para la UI de Compose
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    implementation("androidx.navigation:navigation-compose:2.7.3")



        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1") // Para ViewModel en Compose
        implementation("androidx.compose.material3:material3:1.1.0") // Para Material 3
        implementation("androidx.compose.ui:ui:1.5.1") // Para Compose UI
        implementation("androidx.compose.ui:ui-tooling-preview:1.5.1") // Para vistas previas en Compose
        implementation("androidx.compose.foundation:foundation:1.5.1") // Componentes base de Compose



    // Dependencias de Room
    implementation(libs.androidx.room.roomRuntime)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.appcompat)
    kapt(libs.androidx.room.roomCompiler)
    implementation(libs.androidx.room.roomKtx)

    // Test y Debug
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
