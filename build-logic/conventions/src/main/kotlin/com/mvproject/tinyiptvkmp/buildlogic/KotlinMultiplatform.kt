package com.mvproject.tinyiptvkmp.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension,
) {
    extension.apply {
        androidTarget {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
        iosArm64()
        iosSimulatorArm64()
        jvm("desktop")

        sourceSets {
            commonMain.dependencies {
                implementation(project(":core:foundation"))
                implementation(project(":infrastructure:logging"))
                implementation(library("kotlinx-coroutines-core"))
                implementation(library("koin-core"))
            }
            commonTest.dependencies {
                implementation(library("kotlin-test"))
                implementation(library("kotlinx-coroutines-core"))
                implementation(library("kotlinx-coroutines-test"))
            }
        }
    }

    if (tasks.findByName("testClasses") == null) {
        tasks.register("testClasses")
    }
}
