/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 23.03.24, 20:42
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.mvproject.tinyiptvkmp.data.enums

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_cinematic
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_fullScreen
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_original
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_square
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_wideScreen
import tinyiptvkmp.composeapp.generated.resources.Res

enum class RatioMode(val value: Int, val title: StringResource, val ratio: Float) {
    Original(0, Res.string.video_ratio_mode_original, 1f),
    WideScreen(1, Res.string.video_ratio_mode_wideScreen, 1.777f),
    FullScreen(2, Res.string.video_ratio_mode_fullScreen, 1.333f),
    Cinematic(3, Res.string.video_ratio_mode_cinematic, 2.333f),
    Square(4, Res.string.video_ratio_mode_square, 1f),
    ;

    companion object {
        fun toggleRatioMode(current: RatioMode) =
            when (current) {
                Original -> WideScreen
                WideScreen -> FullScreen
                FullScreen -> Cinematic
                Cinematic -> Square
                Square -> Original
            }
    }
}
