/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 19:00
 *
 */

package com.mvproject.tinyiptv.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.media3.common.VideoSize
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

internal fun VideoSize.aspectRatio(): Float =
    if (height == 0 || width == 0) 0f else (width * pixelWidthHeightRatio) / height

fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("no activity")
}
fun String.getNameFromStringUri() =
    Uri.parse(this).path?.split("/")?.last() ?: EMPTY_STRING
