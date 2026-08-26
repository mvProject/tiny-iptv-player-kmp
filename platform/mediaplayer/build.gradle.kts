plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.foundation)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.infrastructure.logging)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.compose.runtime.retain)
            implementation(libs.bundles.media3)
            implementation(libs.bundles.nextlib)
            implementation(libs.ktor.client.android)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.caprica.vlcj)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.platform.mediaplayer"
}
