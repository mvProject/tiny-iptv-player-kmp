package com.mvproject.tinyiptvkmp.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.versionInt(name: String): Int =
    findVersion(name).get().requiredVersion.toInt()

internal fun Project.library(name: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibrary(name).get()
