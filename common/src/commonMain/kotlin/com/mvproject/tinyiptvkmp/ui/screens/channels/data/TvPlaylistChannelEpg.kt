/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 19:23
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.data

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Immutable
data class TvPlaylistChannelEpg(
    val items: List<EpgProgram> = emptyList(),
)

data class ChannelEpg(
    val channelName: String = String.empty,
    val channelEpgId: String = String.empty,
    val programs: List<EpgProgram> = emptyList(),
)
