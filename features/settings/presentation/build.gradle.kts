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
            implementation(projects.features.epg.api)
            implementation(projects.features.player.api)
            implementation(projects.features.playlist.api)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.lifecycle)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.settings.presentation"
}

compose.resources {
    packageOfResClass =
        "com.mvproject.tinyiptvkmp.features.settings.presentation.generated.resources"
    publicResClass = true
}

koinCompiler {
    compileSafety = false
}
