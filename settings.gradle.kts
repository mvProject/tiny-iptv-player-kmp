/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 30.01.24, 12:10
 *
 */

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "TinyIptvKmp"
include(":composeApp")
include(":core:base")
include(":core:database")
include(":core:datastore")
include(":core:foundation")
include(":core:network")
include(":core:ui")
include(":core:designsystem")
include(":infrastructure:logging")
include(":features:channels-api")
include(":features:groups-api")
include(":features:epg-api")
include(":features:playlist-api")
include(":features:epg")
include(":features:channels")
include(":features:groups")
include(":features:playlist")
include(":features:settings")
include(":platform:mediaplayer")
