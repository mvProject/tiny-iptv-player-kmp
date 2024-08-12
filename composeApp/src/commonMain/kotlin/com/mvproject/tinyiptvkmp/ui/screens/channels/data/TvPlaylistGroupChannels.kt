/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 11:25
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.data

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel

@Immutable
data class TvPlaylistGroupChannels(
    val items: List<TvPlaylistChannel> = emptyList(),
)
