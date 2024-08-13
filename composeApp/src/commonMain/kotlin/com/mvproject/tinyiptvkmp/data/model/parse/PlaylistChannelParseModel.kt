/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.model.parse

import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class PlaylistChannelParseModel(
    val mStreamURL: String,
    val mLogoURL: String = String.empty,
    val mGroupTitle: String = String.empty,
    val mChannel: String = String.empty,
)
