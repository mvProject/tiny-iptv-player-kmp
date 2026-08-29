plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.koin)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.settings.api"
}

koinCompiler {
    compileSafety = false
}
