plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.channels.api)
            implementation(libs.bundles.room)
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

android {
    namespace = "com.mvproject.tinyiptvkmp.persistence.channels.room"
}
