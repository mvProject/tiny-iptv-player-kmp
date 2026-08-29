plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.runtime.compose)
            api(projects.infrastructure.logging)
            implementation(libs.koin.core)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.base"
}
