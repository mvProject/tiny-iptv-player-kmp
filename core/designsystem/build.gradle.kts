plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(libs.compose.material3.adaptive)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.designsystem"
}
