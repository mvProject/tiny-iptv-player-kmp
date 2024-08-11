import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)

            implementation(libs.androidx.compose.activity)

            // Network
            implementation(libs.ktor.client.android)

            // DI
            implementation(libs.koin.android)

            // Exoplayer
            implementation(libs.bundles.media3)

            // Misc
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.accompanist.adaptive)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DI
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.bundles.koin)

            // Storage
            implementation(libs.androidx.datastore.core)

            // Room
            implementation(libs.bundles.room)

            // Network
            implementation(libs.bundles.ktor)

            // Logging
            implementation(libs.kermit)

            // Navigation
            implementation(libs.bundles.navigation)

            // Lifecycle
            implementation(libs.bundles.lifecycle)

            // Misc
            implementation(libs.androidx.annotation)
            implementation(libs.kotlinx.datetime)
            implementation(libs.material3.window.size.multiplatform)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.urikmp)

            // Image processing
            implementation(libs.kamelimage)

            // Resources
            implementation(compose.components.resources)

            implementation(libs.bundles.ksoup)

            implementation(compose.components.uiToolingPreview)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            // Core
            implementation(compose.desktop.common)

            // DI
            implementation(libs.koin.core)

            // Coroutines
            implementation(libs.kotlinx.coroutines.swing)

            // Network
            implementation(libs.ktor.client.java)

            // FilePicker
            implementation(libs.calf.filepicker)

            // Vlc player
            implementation(libs.caprica.vlcj)
        }
    }
}

android {
    namespace = "com.mvproject.tinyiptvkmp"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    //  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.mvproject.tinyiptvkmp"
        minSdk = 26
        targetSdk = 34
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
        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "DesktopAppKt"
        nativeDistributions {
            packageName = "Tiny Iptv"
            packageVersion = "1.0.0"
            description = "Iptv player multiplatform App"
            copyright = "©2023 MvProject. All rights reserved."
            targetFormats(TargetFormat.Exe)

            //  modules("java.base", "java.instrument", "java.management", "java.net.http", "java.sql", "jdk.unsupported", "jdk.xml.dom")
            includeAllModules = true

            windows {
                iconFile.set(project.file("tiny_iptv_kmp.ico"))
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

fun readProperties(propertiesFile: File) =
    Properties().apply {
        propertiesFile.inputStream().use { fis ->
            load(fis)
        }
    }
