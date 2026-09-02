plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(projects.core.foundation)
            implementation(projects.core.network)
            api(projects.features.channels.api)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.protobuf)
            implementation(libs.okio)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.epg.api"
}
