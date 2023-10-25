/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 25.10.23, 10:49
 *
 */

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.mvproject.tinyiptv.common"
    /*  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
      sourceSets["main"].res.srcDirs("src/commonMain/resources")
      sourceSets["main"].resources.srcDirs("src/commonMain/resources")*/
    compileSdk = 34
    defaultConfig {
        minSdk = 26
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
                /*
                       implementation(compose.ui)
                       implementation(compose.material3)
                       implementation(compose.materialIconsExtended)

                         implementation(libs.material3.window.size.multiplatform)

                         implementation(libs.sqldelight.runtime)
                         implementation(libs.sqldelight.coroutines.extensions)

                         implementation(libs.kotlinx.dateTime)

                         implementation(libs.napier)

                         implementation(libs.kotlinx.serializationJson)

                         implementation(libs.kotlinx.collections.immutable)

                         @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                         implementation(compose.components.resources)

                         implementation(libs.ktor.client.core)
                         implementation(libs.ktor.client.logging.jvm)
                         implementation(libs.ktor.client.content.negotiation)
                         implementation(libs.ktor.ktor.serialization.kotlinx.json)

         */
            }
        }

        val androidMain by getting {
            dependencies {
                /*implementation(libs.androidx.compose.activity)
                implementation(libs.androidx.compose.lifecycle.viewmodel)

                implementation(libs.sqldelight.driver.android)

                implementation(libs.accompanist.systemuicontroller)
                implementation(libs.accompanist.adaptive)


                            implementation(libs.core)
                         */
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
                implementation(compose.desktop.common)
                /*
                                implementation(libs.kotlinx.coroutines.swing)

                                // Toaster for Windows
                                implementation(libs.toast4j)
                */
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