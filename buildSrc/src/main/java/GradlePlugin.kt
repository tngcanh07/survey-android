/**
 * Created by toannguyen
 * Jul 12, 2021 at 22:44
 */
object GradlePlugin {
    const val ANDROID_BUILD_GRADLE =
        "com.android.tools.build:gradle:${Versions.ANDROID_BUILD_GRADLE}"
    const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val NAVIGATION_SAFE_ARGS =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
    const val HILT = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
}