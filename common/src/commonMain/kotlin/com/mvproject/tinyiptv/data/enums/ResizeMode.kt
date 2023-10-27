/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 13:32
 *
 */

package com.mvproject.tinyiptv.data.enums

// todo fix string resources

/*enum class ResizeMode(val value: Int, @StringRes val title: Int) {
    Fit(0, R.string.video_resize_mode_fit),
    FixedWidth(1, R.string.video_resize_mode_fixed_width),
    FixedHeight(2, R.string.video_resize_mode_fixed_height),
    Fill(3, R.string.video_resize_mode_fill),
    Zoom(4, R.string.video_resize_mode_zoom);

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
}*/

enum class ResizeMode(val value: Int, val title: String) {
    Fit(0, "Fit"),
    FixedWidth(1, "FixedWidth"),
    FixedHeight(2, "FixedHeight"),
    Fill(3, "Fill"),
    Zoom(4, "Zoom");

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