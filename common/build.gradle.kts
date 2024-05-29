/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 10:17
 *
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.mvproject.tinyiptvkmp"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }
    jvm {
        jvmToolchain(17)
    }

    sourceSets {
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DI
            implementation(libs.bundles.koin)

            // Storage
            implementation(libs.datastore.preferences.core)

            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            // Network
            implementation(libs.bundles.ktor)

            // Logging
            implementation(libs.kermit)

            // Navigation
            implementation(libs.bundles.precompose)

            // Misc
            implementation(libs.androidx.annotation)
            implementation(libs.kotlinx.dateTime)
            implementation(libs.material3.window.size.multiplatform)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.urikmp)

            // Image processing
            implementation(libs.kamelimage)

            // Resources
            implementation(compose.components.resources)
        }

        commonTest.dependencies {
            implementation(kotlin("test-common"))
        }

        androidMain.dependencies {
            // Core
            implementation(libs.androidx.compose.activity)
            implementation(libs.androidx.compose.lifecycle.runtime)

            // Network
            implementation(libs.ktor.client.android)

            // DI
            implementation(libs.bundles.koinAndroid)

            // Exoplayer
            implementation(libs.bundles.media3)

            // Misc
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.accompanist.adaptive)
        }

        jvmMain.dependencies {
            // Core
            implementation(compose.desktop.common)

            // Coroutines
            implementation(libs.kotlinx.coroutines.swing)

            // Network
            implementation(libs.ktor.client.java)
            // implementation(libs.ktor.client.okhttp)

            // Storage
            // implementation(libs.sqldelight.driver.jvm)

            // FilePicker
            implementation(libs.calf.filepicker)

            // Vlc player
            implementation(libs.caprica.vlcj)
        }
    }

    task("testClasses")
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
