/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 31.01.24, 08:26
 *
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.compose.multiplatform)
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

    kotlin {
        jvmToolchain(17)
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
    jvm() {
        jvmToolchain(17)
    }

    sourceSets {
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
            implementation(libs.bundles.sqldelight)

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
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            // Core
            implementation(libs.androidx.compose.activity)
            implementation(libs.androidx.compose.lifecycle.runtime)

            // Storage
            implementation(libs.sqldelight.driver.android)

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

        androidNativeTest.dependencies {
            // implementation(libs.junit)
            // Tests
            /*                testImplementation(libs.test.junit)
                            androidTestImplementation(libs.test.ext.junit)
                            androidTestImplementation(libs.test.espresso.core)

                            // UI Tests
                            androidTestImplementation(libs.androidx.compose.ui.test.junit4)
                            debugImplementation(libs.androidx.compose.ui.test.manifest)

                           */
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
            implementation(libs.sqldelight.driver.jvm)

            // FilePicker
            implementation(libs.calf.filepicker)

            // Vlc player
            implementation(libs.caprica.vlcj)
        }
    }
}

sqldelight {
    databases {
        create("TinyIptvKmpDatabase") {
            packageName.set("com.mvproject.tinyiptvkmp")
        }
    }
}
