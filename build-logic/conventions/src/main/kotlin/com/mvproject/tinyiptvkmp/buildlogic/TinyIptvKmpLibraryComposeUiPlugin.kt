package com.mvproject.tinyiptvkmp.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class TinyIptvKmpLibraryComposeUiPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("tinyiptv.kmp.library.compose")

        configureComposeUi()
    }
}
