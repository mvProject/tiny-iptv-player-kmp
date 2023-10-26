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
    // alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.compose.multiplatform)
    /*    id("io.github.skeptick.libres")
        alias(libs.plugins.mokoResources)*/
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
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation("co.touchlab:kermit:2.0.0-RC4")

                //  implementation(libs.stdlib)

                implementation(libs.datastore.preferences.core)
                implementation(libs.kotlinx.coroutines.core)

                /*          implementation("io.github.skeptick.libres:libres-compose:1.2.0-beta01")
                          api(libs.moko.resources)
                          api(libs.moko.resources.compose)*/

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.kotlinx.dateTime)

                implementation(libs.ktor.ktor.serialization.kotlinx.json)

                /*
                             implementation(libs.material3.window.size.multiplatform)

                             implementation(libs.sqldelight.runtime)
                             implementation(libs.sqldelight.coroutines.extensions)

                             implementation(libs.napier)

                             implementation(libs.kotlinx.collections.immutable)

                             implementation(libs.ktor.client.core)
                             implementation(libs.ktor.client.logging.jvm)
                             implementation(libs.ktor.client.content.negotiation)

             */
            }
        }

        val androidMain by getting {
            dependencies {

                implementation(libs.androidx.compose.activity)
                /*
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
                // implementation(libs.sqldelight.driver.jvm)
                implementation(compose.desktop.common)
                implementation(libs.kotlinx.coroutines.swing)
                /*


                                // Toaster for Windows
                                implementation(libs.toast4j)
                */
            }
        }
    }
}

/*libres {
    generatedClassName = "LibMainRes" // "Res" by default
}

multiplatformResources {
    multiplatformResourcesPackage = "com.mvproject.tinyiptv.common"
}*/

/*
sqldelight {
    databases {
        create("TinyIptvDatabase") {
            packageName.set("com.mvproject.tinyiptv")
        }
    }
}*/
