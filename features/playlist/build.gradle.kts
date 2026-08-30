plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.foundation)
            implementation(projects.core.navigation)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.features.playlistApi)
            implementation(projects.infrastructure.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.lifecycle)
            implementation(libs.bundles.filekit)
            implementation(libs.okio)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.playlist"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.features.playlist.generated.resources"
    publicResClass = true
}

koinCompiler {
    compileSafety = false
}
