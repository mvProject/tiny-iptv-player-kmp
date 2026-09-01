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
            implementation(projects.features.channels.api)
            implementation(projects.features.groups.api)
            implementation(projects.features.epg.api)
            implementation(projects.features.player.api)
            implementation(projects.platform.mediaplayer)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.lifecycle)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.player.presentation"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.features.player.presentation.generated.resources"
    publicResClass = true
}

koinCompiler {
    compileSafety = false
}
