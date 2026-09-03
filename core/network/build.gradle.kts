plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.ksoup)
            implementation(libs.okio)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
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
