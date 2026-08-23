package com.mvproject.tinyiptvkmp.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class TinyIptvKmpApplicationComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("com.android.application")
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure<KotlinMultiplatformExtension> {
            configureKotlinMultiplatform(this)
        }
        configureComposeRuntime()
        configureComposeUi()
        extensions.configure<ApplicationExtension> {
            configureAndroidApplication(this)
            configureAndroidCompose(this)
        }
    }
}
