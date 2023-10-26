/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.model.parse

import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

data class PlaylistChannelParseModel(
    val mStreamURL: String,
    val mLogoURL: String = EMPTY_STRING,
    val mGroupTitle: String = EMPTY_STRING,
    val mChannel: String = EMPTY_STRING
)