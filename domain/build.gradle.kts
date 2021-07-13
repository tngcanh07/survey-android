plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.JAVAX_INJECT)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)
}