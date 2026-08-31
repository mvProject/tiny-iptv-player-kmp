plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.settings.api"
}

koinCompiler {
    compileSafety = false
}
