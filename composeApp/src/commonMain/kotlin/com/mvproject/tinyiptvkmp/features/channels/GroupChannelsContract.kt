/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.compose.runtime.Stable
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

interface GroupChannelsContract {
    @Stable
    data class UiState(
        val currentGroup: String = String.empty,
        val isLoading: Boolean = false,
        val isEpgVisible: Boolean = false,
        val searchString: String = String.empty,
        val viewType: ChannelsViewType = ChannelsViewType.LIST,
        val channels: List<TvChannel> = emptyList(),
        val selectedName: String = String.empty,
        val selectedPrograms: List<EpgProgram> = emptyList()
    )

    sealed interface UiAction {
        data class ToggleFavourites(val channel: TvChannel, val type: FavoriteType) : UiAction
        data class SearchTextChange(val text: String) : UiAction
        data class ViewTypeChange(val type: ChannelsViewType) : UiAction
        data class ToggleEpgVisibility(
            val name: String = String.empty,
            val epgID: String = String.empty
        ) : UiAction

        data class NavigateToSelected(val name: String, val group: String) : UiAction
        data object NavigateBack : UiAction
    }

    sealed interface UiEffect {
        data class NavigateToSelected(val name: String, val group: String) : UiEffect
        data object NavigateBack : UiEffect
    }
}