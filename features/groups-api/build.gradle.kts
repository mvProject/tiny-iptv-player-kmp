plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.channelsApi)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.groups.api"
}
