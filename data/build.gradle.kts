plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))
    implementation(Libs.KOTLIN_STDLIB)

    api(Libs.RETROFIT)
    implementation(Libs.RETROFIT_RX_JAVA3_ADAPTER)
    implementation(Libs.RETROFIT_GSON_CONVERTER)
    implementation(Libs.OKHTTP)
    implementation(Libs.OKHTTP_LOGGING)
    implementation(Libs.GSON)
    implementation(Libs.RX_JAVA3)

    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)
    testImplementation(Libs.MOCK_WEBSERVER)
}