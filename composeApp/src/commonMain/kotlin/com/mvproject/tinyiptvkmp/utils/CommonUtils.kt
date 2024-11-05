/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 19:32
 *
 */

package com.mvproject.tinyiptvkmp.utils

import okio.FileSystem

typealias KLog = co.touchlab.kermit.Logger

object CommonUtils {
    inline val String.Companion.empty get() = ""

    inline val String.Companion.space get() = " "

    inline val String.Companion.delimiterTime get() = ":"

    inline val String.Companion.delimiterDash get() = " - "

    inline val String.Companion.typeM3U get() = "m3u"

    inline val String.Companion.typeM3U8 get() = "m3u8"

    inline val tmpFolder get() = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
}
