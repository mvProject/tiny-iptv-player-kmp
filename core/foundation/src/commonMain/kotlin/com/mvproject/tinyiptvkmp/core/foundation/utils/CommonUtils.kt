package com.mvproject.tinyiptvkmp.core.foundation.utils

object CommonUtils {
    inline val String.Companion.empty get() = ""

    inline val String.Companion.space get() = " "

    inline val String.Companion.delimiterTime get() = ":"

    inline val String.Companion.delimiterDash get() = " - "

    inline val String.Companion.typeM3U get() = "m3u"

    inline val String.Companion.typeM3U8 get() = "m3u8"
}
