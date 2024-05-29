/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 11:59
 *
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":common"))
    implementation(libs.koin.core)
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "DesktopAppKt"
        nativeDistributions {
            packageName = "Tiny Iptv"
            packageVersion = "1.0.0"
            description = "Iptv player multiplatform App"
            copyright = "©2023 MvProject. All rights reserved."
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
            )

            //  modules("java.base", "java.instrument", "java.management", "java.net.http", "java.sql", "jdk.unsupported", "jdk.xml.dom")
            includeAllModules = true

            windows {
                iconFile.set(project.file("tiny_iptv_kmp.ico"))
            }
        }
    }
}
