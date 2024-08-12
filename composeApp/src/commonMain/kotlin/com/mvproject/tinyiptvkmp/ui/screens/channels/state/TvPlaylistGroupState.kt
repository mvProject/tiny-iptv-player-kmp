/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels.state

import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING

data class TvPlaylistGroupState(
    val currentGroup: String = EMPTY_STRING,
    val currentGroupType: String = EMPTY_STRING,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isEpgVisible: Boolean = false,
    val searchString: String = EMPTY_STRING,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
)
