plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.datastore)
            implementation(projects.core.navigation)
            implementation(projects.core.foundation)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.features.channelsApi)
            implementation(projects.features.groupsApi)
            implementation(projects.features.epgApi)
            implementation(projects.infrastructure.logging)
            implementation(projects.platform.mediaplayer)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.lifecycle)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.player"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.features.player.generated.resources"
    publicResClass = true
}

koinCompiler {
    compileSafety = false
}
