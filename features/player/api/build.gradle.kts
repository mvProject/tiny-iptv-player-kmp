plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.foundation)
            api(projects.features.channels.api)
            api(projects.features.groups.api)
            implementation(projects.core.datastore)
            implementation(libs.kotlinx.serialization.protobuf)
            implementation(libs.okio)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.player.api"
}

koinCompiler {
    compileSafety = false
}
