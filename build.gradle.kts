/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 11:57
 *
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlinx.serialization.plugin) apply false
    alias(libs.plugins.compose.compiler) apply false
}
