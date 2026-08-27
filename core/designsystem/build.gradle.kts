plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.foundation)
            implementation(projects.core.ui)
            implementation(libs.compose.material3.adaptive)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.designsystem"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.core.designsystem.generated.resources"
    publicResClass = true
}
