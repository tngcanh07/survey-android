import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

private val localProperties = gradleLocalProperties(rootDir)

fun getProperty(name: String): String? {
    return if (localProperties.containsKey(name)) {
        localProperties.getProperty(name)
    } else {
        null
    }
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    buildToolsVersion(Versions.BUILD_TOOLS)

    defaultConfig {
        applicationId = "com.tn07.survey"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        create("stage") {
            initWith(buildTypes.getAt("debug"))
            applicationIdSuffix = ".stage"
            setMatchingFallbacks("stage", "debug", "release")

            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = getProperty("apiConfigs.staging.baseUrl").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "CLIENT_ID",
                value = getProperty("apiConfigs.staging.clientId").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "CLIENT_SECRET",
                value = getProperty("apiConfigs.staging.clientSecret").orEmpty()
            )
        }

        create("production") {
            initWith(buildTypes.getAt("release"))
            setMatchingFallbacks("production", "release")

            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = getProperty("apiConfigs.production.baseUrl").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "CLIENT_ID",
                value = getProperty("apiConfigs.production.clientId").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "CLIENT_SECRET",
                value = getProperty("apiConfigs.production.clientSecret").orEmpty()
            )
        }
    }

    variantFilter {
        if (name.contains("release", true) || name.contains("debug", true)) {
            ignore = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))
    implementation(project(Modules.DATA))

    implementation(Libs.KOTLIN_STDLIB)

    implementation(Libs.APP_COMPAT)
    implementation(Libs.MATERIAL)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.PAGING_RUNTIME)
    implementation(Libs.PAGING_RXJAVA)
    implementation(Libs.SWIPE_REFRESH_LAYOUT)
    implementation(Libs.NAVIGATION_FRAGMENT)
    implementation(Libs.NAVIGATION_UI)

    implementation(Libs.RX_JAVA3)
    implementation(Libs.RX_ANDROID3)

    implementation(Libs.GLIDE_RUNTIME)
    kapt(Libs.GLIDE_COMPILER)

    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)

    implementation(Libs.RX_BINDING)
    implementation(Libs.BLURRY)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)
}
