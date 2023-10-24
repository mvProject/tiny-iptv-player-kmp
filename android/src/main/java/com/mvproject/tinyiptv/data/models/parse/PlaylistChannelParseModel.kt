/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.models.parse

data class PlaylistChannelParseModel(
    val mStreamURL: String,
    val mLogoURL: String = "",
    val mGroupTitle: String = "",
    val mChannel: String = ""
)