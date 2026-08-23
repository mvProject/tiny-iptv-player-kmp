plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore.core)
            api(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.datastore"
}
