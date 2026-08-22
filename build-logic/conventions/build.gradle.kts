plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "tinyiptv.kmp.library"
            implementationClass = "com.mvproject.tinyiptvkmp.buildlogic.TinyIptvKmpLibraryPlugin"
        }
        register("kmpLibraryCompose") {
            id = "tinyiptv.kmp.library.compose"
            implementationClass =
                "com.mvproject.tinyiptvkmp.buildlogic.TinyIptvKmpLibraryComposePlugin"
        }
        register("kmpLibraryComposeUi") {
            id = "tinyiptv.kmp.library.compose.ui"
            implementationClass =
                "com.mvproject.tinyiptvkmp.buildlogic.TinyIptvKmpLibraryComposeUiPlugin"
        }
        register("kmpApplicationCompose") {
            id = "tinyiptv.kmp.application.compose"
            implementationClass =
                "com.mvproject.tinyiptvkmp.buildlogic.TinyIptvKmpApplicationComposePlugin"
        }
    }
}
