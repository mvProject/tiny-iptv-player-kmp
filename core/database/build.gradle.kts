plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.room)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.database"
}
