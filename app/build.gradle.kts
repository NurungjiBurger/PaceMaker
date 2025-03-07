plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt") // Room annotation processor
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.maker.pacemaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.maker.pacemaker"
        minSdk = 31
        targetSdk = 35
        versionCode = 3
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"${project.findProperty("BASE_URL")}\"")
        buildConfigField("String", "OPEN_API_KEY", "\"${project.findProperty("OPEN_API_KEY")}\"")
        buildConfigField("String", "TEST_BASE_URL", "\"${project.findProperty("TEST_BASE_URL")}\"")
        buildConfigField("String", "CLOVA_TTS_API_URL", "\"${project.findProperty("CLOVA_TTS_API_URL")}\"")
        buildConfigField("String", "CLOVA_STT_API_URL", "\"${project.findProperty("CLOVA_STT_API_URL")}\"")
        buildConfigField("String", "CLOVA_API_CLIENT_ID", "\"${project.findProperty("CLOVA_API_CLIENT_ID")}\"")
        buildConfigField("String", "CLOVA_API_CLIENT_SECRET", "\"${project.findProperty("CLOVA_API_CLIENT_SECRET")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"http://release-api-url.com\"")
            buildConfigField("String", "OPEN_API_KEY", "\"release-api-key\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8" // 또는 "17"로 변경 가능 (코틀린 1.8.0 이상에서 사용)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Compose Compiler 버전도 확인
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // 레이더 차트(점수 시각화)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // 파일 포맷 변경
    implementation("com.arthenica:ffmpeg-kit-full:6.0-2")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.hilt.work)
    kapt("androidx.room:room-compiler:2.6.1") // annotationProcessor는 제거
    implementation("androidx.room:room-ktx:2.6.1")

    // hilt
    implementation("com.google.dagger:hilt-android:2.46.1") // 힐트 버전 업데이트
    kapt("com.google.dagger:hilt-compiler:2.46.1") // Hilt 컴파일러 버전 업데이트

    implementation(libs.androidx.material3.v100alpha06)

    // 지문 인증
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.appcompat)

    // 이미지 캐시
    implementation(libs.coil.compose)

    // constraint layout
    implementation(libs.androidx.constraintlayout.compose)

    // http 통신
    implementation(libs.com.squareup.retrofit2.retrofit3)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.logging.interceptor)

    // firebase cloud messaging
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-messaging-ktx:24.0.3")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")

    // firebase authentication
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")


    // 이미지 로딩
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // mqtt
    implementation(libs.org.eclipse.paho.client.mqttv3)
    implementation(libs.org.eclipse.paho.android.service)

    // JSON
    implementation(libs.jetbrains.kotlinx.serialization.json)

    // preview
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")

    // Coroutine for background processing
    implementation(libs.kotlinx.coroutines.core.v160)
    implementation(libs.kotlinx.coroutines.android.v160)
    implementation(libs.kotlinx.coroutines.android)

    // work manager
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // Hilt WorkManager 통합
    implementation("androidx.hilt:hilt-work:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation("com.google.android.material:material:1.12.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}

