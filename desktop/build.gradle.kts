/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 25.10.23, 10:49
 *
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.compose.multiplatform)
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
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
            )

            //  modules("java.base", "java.instrument", "java.management", "java.net.http", "java.sql", "jdk.unsupported", "jdk.xml.dom")
            includeAllModules = true

            windows {
                iconFile.set(project.file("tiny_iptv_kmp.ico"))
            }
        }
    }
}
