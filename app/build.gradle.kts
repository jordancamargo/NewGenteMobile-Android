plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "br.com.lg.MyWay.newgentemobile"
    compileSdk = 34
    useLibrary("org.apache.http.legacy")

    defaultConfig {
        applicationId = "br.com.lg.MyWay.newgentemobile"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        externalNativeBuild {
            cmake {
                cppFlags.add("-frtti")
                cppFlags.add("-fexceptions")
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
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

    //Bibliotecas Android/Jetbrains
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Bibliotecas do DinamoDB da AWS
    implementation("com.amazonaws:aws-android-sdk-ddb-mapper:2.72.0")

    // Bibliotecas do Firebase
    implementation("com.google.firebase:firebase-messaging:23.2.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-perf:20.4.0")
    implementation("com.google.firebase:firebase-analytics:21.3.0")
    implementation("com.google.firebase:firebase-crashlytics:18.4.0")
    implementation("com.google.firebase:firebase-firestore:24.7.0")

    // Bibliotecas do Google Play Services
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // Bibliotecas de Terceiros
    implementation("com.dynatrace.agent:agent-android:8.271.1.1003")
}

// SOMENTE PARA SANTANDER DYNATRACE
if (gradle.startParameter.taskRequests.toString().contains("Santander") &&
    gradle.startParameter.taskRequests.toString().contains("debug")) {
    println("*** SOMENTE SANTANDER, ATIVANDO PRODUTO DYNATRACE ***")

    apply(plugin = "com.dynatrace.tools.android")

    afterEvaluate {
        configure<Any> {
            extensions.configure<Any>("dynatrace") {
                val defaultConfig = (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("defaultConfig") as org.gradle.api.plugins.ExtensionAware
                defaultConfig.extensions.configure<Any>("applicationId") {
                    setProperty("applicationId", "bb5d22de-d483-4156-b2cc-2e74807c7e4b")
                }
                defaultConfig.extensions.configure<Any>("beaconURL") {
                    setProperty("beaconURL", "https://m.dyna.santander.com.br:443/mbeacon/af726b4e-68a3-4958-8241-92195def8672")
                }
            }
        }
    }
}