/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.models.parse

data class PlaylistChannelParseModel(
    val mStreamURL: String,
    val mLogoURL: String = "",
    val mGroupTitle: String = "",
    val mChannel: String = ""
)