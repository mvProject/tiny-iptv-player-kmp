plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore.core.okio)
            api(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.datastore"
}
