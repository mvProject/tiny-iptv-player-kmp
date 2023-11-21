/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 13:32
 *
 */

package com.mvproject.tinyiptvkmp.data.enums

import com.mvproject.tinyiptvkmp.MainRes

enum class RatioMode(val value: Int, val title: String, val ratio: Float) {
    Original(0, MainRes.string.video_ratio_mode_original, 1f),
    WideScreen(1, MainRes.string.video_ratio_mode_wideScreen, 1.777f),
    FullScreen(2, MainRes.string.video_ratio_mode_fullScreen, 1.333f),
    Cinematic(3, MainRes.string.video_ratio_mode_cinematic, 2.333f),
    Square(4, MainRes.string.video_ratio_mode_square, 1f);

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