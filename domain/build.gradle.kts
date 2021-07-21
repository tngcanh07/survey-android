import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("kotlin")
    jacoco
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

tasks.jacocoTestReport {
    classDirectories.setFrom(fileTree("$buildDir/classes") {
        setExcludes(
            listOf("**/test/**")
        )
    })
    sourceDirectories.setFrom("${project.projectDir}/src/main/java")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"

            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
                includes = listOf("com.tn07.survey.domain.*")
            }
        }
    }
}
