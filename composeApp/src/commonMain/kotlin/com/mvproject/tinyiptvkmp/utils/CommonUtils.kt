/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 19:32
 *
 */

package com.mvproject.tinyiptvkmp.utils

import com.eygraber.uri.Uri
import java.util.Locale

typealias KLog = co.touchlab.kermit.Logger

object CommonUtils {
    // todo uri fix
    fun String.getNameFromStringUri() =
        Uri
            .parse(this)
            .path
            ?.split("/")
            ?.last() ?: AppConstants.EMPTY_STRING

    fun Boolean.toLong() = if (this) 1L else 0L

    fun Long.toBoolean() = this != 0L

    val isWindowsDesktop
        get() =
            System
                .getProperty("os.name", "generic")
                .lowercase(Locale.ENGLISH)
                .contains("windows", ignoreCase = true)

    inline val String.Companion.empty get() = ""

    inline val String.Companion.space get() = " "
}
