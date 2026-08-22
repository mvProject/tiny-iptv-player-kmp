plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.base"
}
