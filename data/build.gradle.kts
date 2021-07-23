plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    jacoco
}

jacoco {
    toolVersion = Versions.JACOCO
}

private val localProperties =
    com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)

fun getProperty(name: String): String? {
    return if (localProperties.containsKey(name)) {
        localProperties.getProperty(name)
    } else {
        null
    }
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        testBuildType = "debug"
    }

    buildTypes {
        named("debug") {
            isTestCoverageEnabled = true

            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = getProperty("apiConfigs.test.baseUrl").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "CLIENT_ID",
                value = getProperty("apiConfigs.test.clientId").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "CLIENT_SECRET",
                value = getProperty("apiConfigs.test.clientSecret").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "TEST_EMAIL",
                value = getProperty("apiConfigs.test.email").orEmpty()
            )
            buildConfigField(
                type = "String",
                name = "TEST_PASSWORD",
                value = getProperty("apiConfigs.test.password").orEmpty()
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}


tasks.create(name = "jacocoTestReport", type = JacocoReport::class) {
    setDependsOn(setOf("testDebugUnitTest", "createDebugCoverageReport"))
    classDirectories.setFrom(fileTree("$buildDir/tmp/kotlin-classes/debug") {
        setExcludes(
            listOf(
                "**/test/**",
                "**/com/tn07/survey/data/di/**.class"
            )
        )
    })
    sourceDirectories.setFrom("$projectDir/src/main/java")
    executionData.setFrom(fileTree(projectDir) {
        setIncludes(
            listOf(
                "$buildDir/jacoco/testDebugUnitTest.exec",
                "jacoco.exec"
            )
        )
    })
}

tasks.create(name = "jacocoTestCoverageVerification", type = JacocoCoverageVerification::class) {
    setDependsOn(setOf("jacocoTestReport"))
    classDirectories.setFrom(fileTree("$buildDir/tmp/kotlin-classes/debug") {
        setExcludes(
            listOf(
                "**/test/**",
                "**/com/tn07/survey/data/di/**.class"
            )
        )
    })
    executionData.setFrom(fileTree(projectDir) {
        setIncludes(
            listOf(
                "$buildDir/jacoco/testDebugUnitTest.exec",
                "jacoco.exec"
            )
        )
    })
    violationRules {
        rule {
            element = "PACKAGE"
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
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

    testImplementation(Libs.JUNIT4)
    testImplementation(Libs.MOCKITO)
    testImplementation(Libs.MOCK_WEBSERVER)
    testImplementation(Libs.ROBOLECTRIC)
}
