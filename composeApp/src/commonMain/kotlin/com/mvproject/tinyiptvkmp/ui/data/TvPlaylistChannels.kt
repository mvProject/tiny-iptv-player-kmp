/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:29
 *
 */

package com.mvproject.tinyiptvkmp.ui.data

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel

@Immutable
data class TvPlaylistChannels(
    val items: List<TvPlaylistChannel> = emptyList(),
)
