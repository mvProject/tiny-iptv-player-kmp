/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 13:32
 *
 */

package com.mvproject.tinyiptvkmp.data.enums

// todo fix string resources

enum class RatioMode(val value: Int, val title: String, val ratio: Float) {
    Original(0, "Video", 1f),
    WideScreen(1, "16:9", 1.777f),
    FullScreen(2, "4:3", 1.333f),
    Cinematic(3, "21:9", 2.333f),
    Square(4, "1:1", 1f);

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