plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
        }

        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.compose.runtime.retain)
            implementation(libs.bundles.media3)
            implementation(libs.bundles.nextlib)
            implementation(libs.ktor.client.okhttp)
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
