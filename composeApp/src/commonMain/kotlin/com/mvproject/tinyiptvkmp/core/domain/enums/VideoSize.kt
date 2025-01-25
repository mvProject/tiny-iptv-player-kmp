/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.FitScreen
import androidx.compose.material.icons.rounded.Height
import androidx.compose.material.icons.rounded.WidthWide
import androidx.compose.material.icons.rounded.ZoomOut
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_cinematic
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_fullScreen
import tinyiptvkmp.composeapp.generated.resources.video_ratio_mode_wideScreen
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_fill
import tinyiptvkmp.composeapp.generated.resources.video_resize_mode_fit

enum class VideoSize {
    Cinematic,
    FitScreen,
    WideScreen,
    FillScreen,
    FullScreen
    ;

    companion object {
        fun toggleVideoSize(current: VideoSize) =
            when (current) {
                Cinematic -> FitScreen
                FitScreen -> WideScreen
                WideScreen -> FillScreen
                FillScreen -> FullScreen
                FullScreen -> Cinematic
            }

        fun VideoSize.mapToString() = when (this) {
            Cinematic -> Res.string.video_ratio_mode_cinematic
            FitScreen -> Res.string.video_resize_mode_fit
            WideScreen -> Res.string.video_ratio_mode_wideScreen
            FillScreen -> Res.string.video_resize_mode_fill
            FullScreen -> Res.string.video_ratio_mode_fullScreen
        }

        fun VideoSize.mapToIcon() = when (this) {
            Cinematic -> Icons.Rounded.AspectRatio
            FitScreen -> Icons.Rounded.FitScreen
            WideScreen -> Icons.Rounded.WidthWide
            FillScreen -> Icons.Rounded.Height
            FullScreen -> Icons.Rounded.ZoomOut
        }

        // Icons.Rounded.ZoomIn
    }
}