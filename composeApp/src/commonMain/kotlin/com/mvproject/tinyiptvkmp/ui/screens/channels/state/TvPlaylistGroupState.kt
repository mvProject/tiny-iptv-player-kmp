/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.state

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Immutable
data class TvPlaylistGroupState(
    val currentGroup: String = String.empty,
    val isLoading: Boolean = false,
    val isEpgVisible: Boolean = false,
    val searchString: String = String.empty,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
    val channels: List<TvPlaylistChannel> = emptyList(),
    val selectedName: String = String.empty,
    val selectedPrograms:List<EpgProgram> = emptyList()
)