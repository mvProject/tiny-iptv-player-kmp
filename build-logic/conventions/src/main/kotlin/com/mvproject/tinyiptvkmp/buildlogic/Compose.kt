package com.mvproject.tinyiptvkmp.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun configureAndroidCompose(
    extension: CommonExtension<*, *, *, *, *, *>,
) {
    extension.buildFeatures {
        compose = true
    }
}

internal fun Project.configureComposeRuntime() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            commonMain.dependencies {
                implementation(library("compose-runtime"))
            }
        }
    }
}

internal fun Project.configureComposeReports() {
    if (providers.gradleProperty("composeReports").orNull != "true") {
        return
    }

    pluginManager.withPlugin("org.jetbrains.kotlin.plugin.compose") {
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            reportsDestination.set(layout.buildDirectory.dir("reports/compose-compiler"))
            metricsDestination.set(layout.buildDirectory.dir("reports/compose-compiler"))
        }
    }
}

internal fun Project.configureComposeUi() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            commonMain.dependencies {
                implementation(library("compose-foundation"))
                implementation(library("compose-material3"))
                implementation(library("compose-ui"))
                implementation(library("compose-components-resources"))
                implementation(library("compose-material-icons-extended"))
                implementation(library("compose-ui-tooling-preview"))
            }
            androidMain.dependencies {
                implementation(library("compose-ui-tooling"))
                implementation(library("compose-ui-tooling-preview"))
            }
        }
    }
}
