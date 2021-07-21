import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    jacoco
}

jacoco {
    toolVersion = Versions.JACOCO
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
        testBuildType = "debug"
    }

    buildTypes {
        named("debug") {
            isTestCoverageEnabled = true
        }
    }

    flavorDimensions("app")
    productFlavors {
        create("production") {
            dimension = "app"

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

        create("staging") {
            applicationIdSuffix = ".staging"
            dimension = "app"

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
    }

    variantFilter {
        if (name == "productionDebug" || name == "stagingRelease") {
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

    tasks.withType<Test> {
        useJUnitPlatform()
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
        }
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))
    implementation(project(Modules.DATA))

    implementation(Libs.KOTLIN_STDLIB)

    implementation(Libs.APP_COMPAT)
    implementation(Libs.MATERIAL)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.SWIPE_REFRESH_LAYOUT)
    implementation(Libs.NAVIGATION_FRAGMENT)
    implementation(Libs.NAVIGATION_UI)
    implementation(Libs.SHIMMER)
    implementation(Libs.INDEFINITE_PAGER_INDICATOR)

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

tasks.create(name = "jacocoStagingTestReport", type = JacocoReport::class) {
    setDependsOn(setOf("testStagingDebugUnitTest", "createStagingDebugCoverageReport"))
    classDirectories.setFrom(fileTree("$buildDir/tmp/kotlin-classes/stagingDebug") {
        setExcludes(
            listOf(
                "**/com/tn07/survey/di/**.class"
            )
        )
    })
    sourceDirectories.setFrom("$projectDir/src/main/java")
    executionData.setFrom(fileTree(projectDir) {
        setIncludes(
            listOf(
                "$buildDir/jacoco/testStagingDebugUnitTest.exec",
                "jacoco.exec"
            )
        )
    })
}
