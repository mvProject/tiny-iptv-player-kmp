/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 13:32
 *
 */

package com.mvproject.tinyiptv.data.enums

import com.mvproject.tinyiptv.MainRes

enum class ResizeMode(val value: Int, val title: String) {
    Fit(0, MainRes.string.video_resize_mode_fit),
    FixedWidth(1, MainRes.string.video_resize_mode_fixed_width),
    FixedHeight(2, MainRes.string.video_resize_mode_fixed_height),
    Fill(3, MainRes.string.video_resize_mode_fill),
    Zoom(4, MainRes.string.video_resize_mode_zoom);

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