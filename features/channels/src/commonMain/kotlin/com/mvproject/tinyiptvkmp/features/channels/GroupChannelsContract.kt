package com.mvproject.tinyiptvkmp.features.channels

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

@Immutable
data class GroupChannelsUiState(
    val currentGroup: String = String.empty,
    val isLoading: Boolean = false,
    val searchString: String = String.empty,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
    val channels: List<TvChannelWithPrograms> = emptyList(),
    val selectedName: String = String.empty,
    val selectedPrograms: List<EpgProgram> = emptyList(),
    val osdType: GroupChannelsOSD? = null
) {
    sealed interface GroupChannelsOSD {
        data class ChannelPrograms(val channel: TvChannelWithPrograms) : GroupChannelsOSD
        data class ChannelFavorites(val channel: TvChannelWithPrograms) : GroupChannelsOSD
    }
}

sealed interface GroupChannelsUiAction {
    data class ToggleFavorite(
        val channel: TvChannelWithPrograms,
        val type: FavoriteType
    ) : GroupChannelsUiAction

    data class SearchTextChange(val text: String) : GroupChannelsUiAction
    data class ViewTypeChange(val type: ChannelsViewType) : GroupChannelsUiAction
    data class SelectChannel(
        val name: String,
        val group: String
    ) : GroupChannelsUiAction

    data object NavigateBack : GroupChannelsUiAction
    data class OpenOsd(val type: GroupChannelsUiState.GroupChannelsOSD) : GroupChannelsUiAction
    data object CloseOsd : GroupChannelsUiAction
}

sealed interface GroupChannelsUiEffect


data class GroupChannelsArgs(
    val group: String,
    val groupType: String,
)