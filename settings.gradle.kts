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
include(":core:navigation")
include(":core:network")
include(":core:ui")
include(":core:designsystem")
include(":infrastructure:logging")
include(":persistence:channels-room")
include(":persistence:epg-room")
include(":persistence:playlist-room")
include(":features:channels:api")
include(":features:channels:presentation")
include(":features:groups:api")
include(":features:groups:presentation")
include(":features:epg:api")
include(":features:playlist:api")
include(":features:playlist:presentation")
include(":features:player:api")
include(":features:player:presentation")
include(":features:settings:api")
include(":features:settings:presentation")
include(":platform:mediaplayer")
