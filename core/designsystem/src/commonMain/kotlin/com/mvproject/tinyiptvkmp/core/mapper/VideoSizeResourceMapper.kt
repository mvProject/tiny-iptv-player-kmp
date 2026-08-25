package com.mvproject.tinyiptvkmp.core.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.FitScreen
import androidx.compose.material.icons.rounded.Height
import androidx.compose.material.icons.rounded.WidthWide
import androidx.compose.material.icons.rounded.ZoomOut
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.video_ratio_mode_cinematic
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.video_ratio_mode_fullScreen
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.video_ratio_mode_wideScreen
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.video_resize_mode_fill
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.video_resize_mode_fit
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize

fun VideoSize.mapToString() =
    when (this) {
        VideoSize.Cinematic -> Res.string.video_ratio_mode_cinematic
        VideoSize.FitScreen -> Res.string.video_resize_mode_fit
        VideoSize.WideScreen -> Res.string.video_ratio_mode_wideScreen
        VideoSize.FillScreen -> Res.string.video_resize_mode_fill
        VideoSize.FullScreen -> Res.string.video_ratio_mode_fullScreen
    }

fun VideoSize.mapToIcon() =
    when (this) {
        VideoSize.Cinematic -> Icons.Rounded.AspectRatio
        VideoSize.FitScreen -> Icons.Rounded.FitScreen
        VideoSize.WideScreen -> Icons.Rounded.WidthWide
        VideoSize.FillScreen -> Icons.Rounded.Height
        VideoSize.FullScreen -> Icons.Rounded.ZoomOut
    }
