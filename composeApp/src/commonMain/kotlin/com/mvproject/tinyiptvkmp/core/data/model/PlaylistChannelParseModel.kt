/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.data.model

import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class PlaylistChannelParseModel(
    val streamURL: String,
    val logoURL: String = String.empty,
    val groupTitle: String = String.empty,
    val channel: String = String.empty,
)
