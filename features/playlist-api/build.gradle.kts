plugins {
    alias(libs.plugins.tinyiptv.kmp.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.room)
            implementation(projects.features.channelsApi)
            implementation(projects.core.database)
            implementation(projects.core.datastore)
            implementation(projects.infrastructure.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.mvproject.tinyiptvkmp.features.playlist.api"
}
