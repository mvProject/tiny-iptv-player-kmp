/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 25.10.23, 10:49
 *
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.libres.plugin)
}

libres {
    generatedClassName = "MainRes" // "Res" by default
}

android {
    namespace = "com.mvproject.tinyiptv"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    // sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

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
    jvm("desktop") {
        jvmToolchain(17)
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Core
                implementation(libs.androidx.annotation)

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // DI
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                // Storage
                implementation(libs.datastore.preferences.core)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines.extensions)

                // Network
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)

                // Logging
                implementation(libs.napier)
                implementation(libs.kermit)

                // Navigation
                implementation(libs.voyager.koin)
                implementation(libs.voyager.navigator)

                // Misc
                implementation(libs.kotlinx.dateTime)
                implementation(libs.material3.window.size.multiplatform)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.urikmp)

                // Image
                implementation(libs.kamelimage)

                // Resources
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.libres.compose)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                // Tests
                /*                testImplementation(libs.test.junit)
                                androidTestImplementation(libs.test.ext.junit)
                                androidTestImplementation(libs.test.espresso.core)

                                // UI Tests
                                androidTestImplementation(libs.androidx.compose.ui.test.junit4)
                                debugImplementation(libs.androidx.compose.ui.test.manifest)

                               */
            }
        }
        val androidMain by getting {
            dependencies {
                // Core
                implementation(libs.androidx.compose.activity)
                implementation(libs.androidx.compose.lifecycle.runtime)

                // Storage
                implementation(libs.sqldelight.driver.android)

                // Network
                implementation(libs.ktor.client.android)

                // DI
                implementation(libs.koin.android.compose)
                implementation(libs.koin.android)

                // Misc
                implementation(libs.kotlinx.collections.immutable)

                // Exoplayer
                implementation(libs.media3.exoplayer)
                implementation(libs.media3.ui)
                implementation(libs.media3.exoplayer.hls)

                // Image processing
                implementation(libs.coil)
                implementation(libs.coil.compose)
            }
        }

        val desktopMain by getting {
            dependencies {
                // Core
                implementation(compose.desktop.common)

                // Coroutines
                implementation(libs.kotlinx.coroutines.swing)

                // Network
                // implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.client.java)

                // Storage
                implementation(libs.sqldelight.driver.jvm)

                // FilePicker
                implementation(libs.calf.filepicker)

                // Vlc player
                implementation(libs.caprica.vlcj)
            }
        }
    }
}

sqldelight {
    databases {
        create("TinyIptvDatabase") {
            packageName.set("com.mvproject.tinyiptv")
        }
    }
}
