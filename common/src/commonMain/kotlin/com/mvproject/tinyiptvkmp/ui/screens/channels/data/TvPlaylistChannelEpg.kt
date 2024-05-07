/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 11:29
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.data

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram

@Immutable
data class TvPlaylistChannelEpg(
    val items: List<EpgProgram> = emptyList(),
)
