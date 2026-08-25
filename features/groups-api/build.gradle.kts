plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.channelsApi)
            implementation(projects.infrastructure.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.groups.api"
}
