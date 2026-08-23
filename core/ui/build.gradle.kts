plugins {
    alias(libs.plugins.tinyiptv.kmp.library.compose.ui)
}

android {
    namespace = "com.mvproject.tinyiptvkmp.core.ui"
}

compose.resources {
    packageOfResClass = "com.mvproject.tinyiptvkmp.core.ui.generated.resources"
}
