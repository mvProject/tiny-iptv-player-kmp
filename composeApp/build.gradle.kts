import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            //  binaryOption("bundleId", "com.mvproject.tinyiptvkmp")
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlin.ExperimentalUnsignedTypes,kotlin.RequiresOptIn",
                        "-Xexpect-actual-classes"
                    )
                }
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":core:base"))
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":core:network"))
            implementation(project(":core:ui"))
            implementation(project(":core:designsystem"))
            implementation(project(":infrastructure:logging"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DI
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.bundles.koin)

            // Navigation
            implementation(libs.bundles.navigation)

            // Lifecycle
            implementation(libs.bundles.lifecycle)

            // Misc
            implementation(libs.androidx.annotation)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.compose.material3.adaptive)

            // Image processing
            implementation(libs.bundles.coil)

            // Resources
            implementation(libs.bundles.ksoup)

            implementation(libs.bundles.filekit)
        }

        androidMain.dependencies {
            implementation(compose.preview)

            implementation(libs.androidx.compose.activity)

            // DI
            implementation(libs.koin.android)

            // Exoplayer
            implementation(libs.bundles.media3)

            // Misc
            implementation(libs.kotlinx.collections.immutable)

            implementation(libs.bundles.nextlib)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            // Core
            implementation(compose.desktop.common)

            // DI
            implementation(libs.koin.core)

            // Coroutines
            implementation(libs.kotlinx.coroutines.swing)

            // Vlc player
            implementation(libs.caprica.vlcj)
        }

        nativeMain.dependencies {
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp"
    compileSdk = 36

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        applicationId = "com.mvproject.tinyiptvkmp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    val projectProperties = readProperties(file("../keystore.properties"))
    signingConfigs {
        register("configRelease").configure {
            storeFile = file(projectProperties["storeFile"] as String)
            storePassword = projectProperties["storePassword"] as String
            keyAlias = projectProperties["keyAlias"] as String
            keyPassword = projectProperties["keyPassword"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}",
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("configRelease")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        //     implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            packageName = "Tiny Iptv"
            packageVersion = "1.0.0"
            description = "Iptv player multiplatform App"
            copyright = "©2023 MvProject. All rights reserved."
            targetFormats(TargetFormat.Exe, TargetFormat.Dmg)

            //  modules("java.base", "java.instrument", "java.management", "java.net.http", "java.sql", "jdk.unsupported", "jdk.xml.dom")
            includeAllModules = true

            windows {
                iconFile.set(project.file("tiny_iptv_kmp.ico"))
            }
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

/*room {
    schemaDirectory("$projectDir/schemas")
}*/
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

fun readProperties(propertiesFile: File) =
    Properties().apply {
        propertiesFile.inputStream().use { fis ->
            load(fis)
        }
    }
