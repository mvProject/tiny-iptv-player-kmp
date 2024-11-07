/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.state

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

@Immutable
data class GroupChannelsState(
    val currentGroup: String = String.empty,
    val isLoading: Boolean = false,
    val isEpgVisible: Boolean = false,
    val searchString: String = String.empty,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
    val channels: List<TvChannel> = emptyList(),
    val selectedName: String = String.empty,
    val selectedPrograms:List<EpgProgram> = emptyList()
)