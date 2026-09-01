package com.mvproject.tinyiptvkmp.features.channels.presentation

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

@Immutable
data class GroupChannelsState(
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

sealed interface GroupChannelsAction {
    data class ToggleFavorite(
        val channel: TvChannelWithPrograms,
        val type: FavoriteType
    ) : GroupChannelsAction

    data class SearchTextChange(val text: String) : GroupChannelsAction
    data class ViewTypeChange(val type: ChannelsViewType) : GroupChannelsAction
    data class SelectChannel(
        val name: String,
        val group: String
    ) : GroupChannelsAction

    data object NavigateBack : GroupChannelsAction
    data class OpenOsd(val type: GroupChannelsState.GroupChannelsOSD) : GroupChannelsAction
    data object CloseOsd : GroupChannelsAction
}

sealed interface GroupChannelsEffect


data class GroupChannelsArgs(
    val playlistId: String,
    val group: String,
    val groupType: String,
)
