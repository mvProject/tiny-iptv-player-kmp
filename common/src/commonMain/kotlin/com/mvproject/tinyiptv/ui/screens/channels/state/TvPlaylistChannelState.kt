/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 06.09.23, 19:22
 *
 */

package com.mvproject.tinyiptv.ui.screens.channels.state

import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.utils.AppConstants

data class TvPlaylistChannelState(
    val currentGroup: String = AppConstants.EMPTY_STRING,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isEpgVisible: Boolean = false,
    val searchString: String = AppConstants.EMPTY_STRING,
    val viewType: ChannelsViewType = ChannelsViewType.LIST
)