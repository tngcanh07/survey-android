// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(GradlePlugin.ANDROID_BUILD_GRADLE)
        classpath(GradlePlugin.KOTLIN_GRADLE)
        classpath(GradlePlugin.HILT)
        classpath(GradlePlugin.NAVIGATION_SAFE_ARGS)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
