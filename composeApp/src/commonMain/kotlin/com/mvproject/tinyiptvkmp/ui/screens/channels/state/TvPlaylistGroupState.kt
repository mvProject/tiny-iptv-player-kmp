/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.state

import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class TvPlaylistGroupState(
    val currentGroup: String = String.empty,
    val currentGroupType: String = String.empty,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isEpgVisible: Boolean = false,
    val searchString: String = String.empty,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
)
