package com.mvproject.tinyiptvkmp.platform.mediaplayer

import androidx.media3.common.PlaybackException
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import org.koin.core.component.KoinComponent

private object MediaPlayableCheckLogger : KoinComponent {
    val logger by injectLogger("MediaPlayableCheck")
}

actual fun isMediaPlayable(errorCode: Int?): Boolean {
    val isMediaPlayable =
        when (errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> false
            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> false
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> false
            else -> true
        }
    MediaPlayableCheckLogger.logger.e { "testing errorCode:$errorCode, isMediaPlayable:$isMediaPlayable" }
    return isMediaPlayable
}
