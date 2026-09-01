/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.api.data.datasource.content

internal data class PlaylistChannelParseModel(
    val streamURL: String,
    val logoURL: String = "",
    val groupTitle: String = "",
    val channel: String = "",
)
