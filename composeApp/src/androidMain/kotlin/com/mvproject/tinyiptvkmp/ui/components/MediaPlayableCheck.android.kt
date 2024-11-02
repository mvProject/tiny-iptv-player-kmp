package com.mvproject.tinyiptvkmp.ui.components

import androidx.media3.common.PlaybackException
import com.mvproject.tinyiptvkmp.utils.KLog

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    val isMediaPlayable =
        when (errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> false
            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> false
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> false
            else -> true
        }
    KLog.e("testing errorCode:$errorCode, isMediaPlayable:$isMediaPlayable")
    return isMediaPlayable
}