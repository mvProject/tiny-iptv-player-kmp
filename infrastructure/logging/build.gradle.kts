plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kermit)
            api(libs.ktor.client.logging)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.infrastructure.logging"
}
