import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = Versions.JVM_TARGET
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.RX_JAVA3)
    implementation(Libs.JAVAX_INJECT)

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO)
}