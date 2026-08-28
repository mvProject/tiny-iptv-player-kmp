plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.foundation)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.features.epgApi)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.epg"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.features.epg.generated.resources"
    publicResClass = true
}
