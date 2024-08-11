/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.mvproject.tinyiptvkmp.data.enums

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_fill
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_fit
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_fixed_height
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_fixed_width
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_zoom
import tinyiptvkmp.composeapp.generated.resources.Res

enum class ResizeMode(val value: Int, val title: StringResource) {
    Fit(0, Res.string.video_resize_mode_fit),
    FixedWidth(1, Res.string.video_resize_mode_fixed_width),
    FixedHeight(2, Res.string.video_resize_mode_fixed_height),
    Fill(3, Res.string.video_resize_mode_fill),
    Zoom(4, Res.string.video_resize_mode_zoom),
    ;

    companion object {
        fun toggleResizeMode(current: ResizeMode) =
            when (current) {
                Fit -> Fill
                Fill -> Zoom
                Zoom -> FixedHeight
                FixedHeight -> FixedWidth
                FixedWidth -> Fit
            }
    }
}
