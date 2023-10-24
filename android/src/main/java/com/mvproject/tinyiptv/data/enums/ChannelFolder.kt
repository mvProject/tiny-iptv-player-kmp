/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.enums

import androidx.annotation.StringRes
import com.mvproject.tinyiptv.R

enum class ChannelFolder(@StringRes val title: Int) {
    ALL(R.string.channel_folder_all),
    FAVORITE(R.string.channel_folder_favorite)
}