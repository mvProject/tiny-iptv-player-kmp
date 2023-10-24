/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.enums

import androidx.annotation.StringRes
import com.mvproject.tinyiptv.R

enum class ResizeMode(val value: Int, @StringRes val title: Int) {
    Fit(0, R.string.video_resize_mode_fit),
    FixedWidth(1, R.string.video_resize_mode_fixed_width),
    FixedHeight(2, R.string.video_resize_mode_fixed_height),
    Fill(3, R.string.video_resize_mode_fill),
    Zoom(4, R.string.video_resize_mode_zoom);

    companion object {
        fun toggleResizeMode(current: ResizeMode) =
            when (current) {
                Fit -> {
                    Fill
                }

                Fill -> {
                    Zoom
                }

                Zoom -> {
                    FixedHeight
                }

                FixedHeight -> {
                    FixedWidth
                }

                FixedWidth -> {
                    Fit
                }
            }
    }
}