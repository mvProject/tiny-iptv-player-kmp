plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.channels.api)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.groups.api"
}
