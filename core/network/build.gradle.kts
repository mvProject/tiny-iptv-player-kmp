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
            implementation(libs.koin.core)
            implementation(libs.okio)
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
