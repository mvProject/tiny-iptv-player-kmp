/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.parse

import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING

data class PlaylistChannelParseModel(
    val mStreamURL: String,
    val mLogoURL: String = EMPTY_STRING,
    val mGroupTitle: String = EMPTY_STRING,
    val mChannel: String = EMPTY_STRING
)