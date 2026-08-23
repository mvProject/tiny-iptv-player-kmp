import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.Properties

plugins {
    alias(libs.plugins.tinyiptv.kmp.application.compose)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
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

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.core.database)
            implementation(projects.core.datastore)
            implementation(projects.core.network)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.infrastructure.logging)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // DI
            implementation(libs.bundles.koin)

            // Navigation
            implementation(libs.bundles.navigation)

            // Lifecycle
            implementation(libs.bundles.lifecycle)

            // Misc
            implementation(libs.androidx.annotation)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.protobuf)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.compose.material3.adaptive)

            // Image processing
            implementation(libs.bundles.coil)

            // Resources
            implementation(libs.bundles.ksoup)

            implementation(libs.bundles.filekit)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
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
            implementation(libs.desktop)

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

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        applicationId = "com.mvproject.tinyiptvkmp"
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

koinCompiler {
    compileSafety = true
}

fun readProperties(propertiesFile: File) =
    Properties().apply {
        propertiesFile.inputStream().use { fis ->
            load(fis)
        }
    }
