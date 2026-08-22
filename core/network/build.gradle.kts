plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

kotlin {
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(libs.bundles.ktor)
            implementation(projects.infrastructure.logging)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.network"
}
