package com.mvproject.tinyiptvkmp.buildlogic

import org.gradle.api.Project
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
    }

    if (tasks.findByName("testClasses") == null) {
        tasks.register("testClasses")
    }
}
