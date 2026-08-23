package com.mvproject.tinyiptvkmp.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroidLibrary(
    extension: LibraryExtension,
) {
    extension.apply {
        compileSdk = libs.versionInt("compileSdk")
        defaultConfig {
            minSdk = libs.versionInt("minSdk")
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}

internal fun Project.configureAndroidApplication(
    extension: ApplicationExtension,
) {
    extension.apply {
        compileSdk = libs.versionInt("compileSdk")
        defaultConfig {
            minSdk = libs.versionInt("minSdk")
            targetSdk = libs.versionInt("targetSdk")
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
