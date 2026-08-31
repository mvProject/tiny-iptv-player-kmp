plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore.core.okio)
            api(libs.koin.core)
            implementation(libs.kotlinx.serialization.protobuf)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.datastore"
}
