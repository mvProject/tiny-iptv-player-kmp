plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.navigation)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.features.epgApi)
            implementation(projects.features.playerApi)
            implementation(projects.features.playlistApi)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.lifecycle)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.settings"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.features.settings.generated.resources"
    publicResClass = true
}

koinCompiler {
    compileSafety = false
}
