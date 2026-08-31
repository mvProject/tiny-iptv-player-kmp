plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.base"
}
