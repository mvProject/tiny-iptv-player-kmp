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
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.mvproject.tinyiptv.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

kotlin {
    androidTarget {

    }
    jvm("desktop") {
        jvmToolchain(11)
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation(libs.datastore.preferences.core)
                implementation(libs.kotlinx.coroutines.core)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.kotlinx.dateTime)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines.extensions)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.androidx.annotation)

                implementation(libs.napier)
                implementation(libs.kermit)

                implementation(libs.material3.window.size.multiplatform)
                implementation(libs.kotlinx.collections.immutable)

                // Navigation
                implementation(libs.voyager.koin)
                implementation(libs.voyager.navigator)

                implementation(libs.urikmp)
                implementation(libs.kamelimage)
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

                                // Android Studio Preview support
                                debugImplementation(libs.androidx.compose.ui.tooling)*/
            }
        }
        val androidMain by getting {
            dependencies {

                implementation(libs.androidx.compose.activity)

                implementation(libs.sqldelight.driver.android)

                implementation(libs.koin.android)

                implementation(libs.ktor.client.android)

                // DI
                implementation(libs.koin.android.compose)

                // Misc
                implementation(libs.kotlinx.collections.immutable)

                implementation(libs.androidx.compose.ui.tooling.preview)

                implementation(libs.androidx.compose.lifecycle.runtime)

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
                implementation(compose.desktop.common)
                implementation(libs.kotlinx.coroutines.swing)
                // implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.client.java)
                implementation(libs.sqldelight.driver.jvm)
                implementation(libs.calf.filepicker)
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
