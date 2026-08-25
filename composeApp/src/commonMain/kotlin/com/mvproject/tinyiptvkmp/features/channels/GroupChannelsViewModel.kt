/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.domain.actualDate
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType.Companion.mapViewType
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsUiState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapProgramIds
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.replaceUpdated
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.toggleFavorite
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.withPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    @InjectedParam args: AppRoutes.TvPlaylistChannels,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : ViewModel(),
    MviCore<GroupChannelsUiState, GroupChannelsUiAction, GroupChannelsUiEffect> by mviCore(
        GroupChannelsUiState()
    ) {

    private val group = args.group
    private val type = args.groupType

    private var lastRefresh: Long = 0

    // todo refresh after return from playback

    init {
        viewModelScope.launch {
            val viewType = preferencesStore.data.first().channelsViewType.mapViewType()
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = type)
            updateUiState {
                copy(
                    viewType = viewType,
                    currentGroup = group,
                    channels = groupChannels.withPrograms()
                )
            }
        }
    }

    override fun onAction(uiAction: GroupChannelsUiAction) {
        when (uiAction) {
            GroupChannelsUiAction.NavigateBack -> viewModelScope.postUiEffect(GroupChannelsUiEffect.OnNavigateBack)
            is GroupChannelsUiAction.SelectChannel -> viewModelScope.postUiEffect(
                GroupChannelsUiEffect.OnNavigateToPlayer(
                    name = uiAction.name,
                    group = uiAction.group,
                    groupType = type
                )
            )

            is GroupChannelsUiAction.SearchTextChange -> searchTextChange(text = uiAction.text)
            is GroupChannelsUiAction.ToggleFavorite -> toggleFavorites(
                channel = uiAction.channel,
                type = uiAction.type
            )

            is GroupChannelsUiAction.ViewTypeChange -> viewTypeChange(type = uiAction.type)
            GroupChannelsUiAction.CloseOsd -> closeOsd()
            is GroupChannelsUiAction.OpenOsd -> openOsd(type = uiAction.type)
        }
    }

    fun loadChannelsByGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgPrograms()
        }
    }

    private fun openOsd(type: GroupChannelsOSD) {
        if (type is GroupChannelsOSD.ChannelPrograms) {
            toggleProgramVisibility(
                name = type.channel.channelName,
                programId = type.channel.programId
            )
        }
        updateUiState {
            copy(osdType = type)
        }
    }

    private fun closeOsd() {
        updateUiState {
            copy(osdType = null)
        }
    }

    private suspend fun refreshEpgPrograms() {
        if (actualDate - lastRefresh < 1.minutes.inWholeMilliseconds) {
            return
        }
        val channels = uiState.value.channels
        val channelsIds = channels.mapProgramIds()

        if (channelsIds.isNotEmpty()) {
            val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
            if (channelsEpgData.entries.isNotEmpty()) {
                val channelsWithPrograms = channels.mapPrograms(channelEpgMap = channelsEpgData)

                updateUiState {
                    copy(channels = channelsWithPrograms)
                }
            }

            lastRefresh = actualDate
        }
    }

    private fun searchTextChange(text: String) {
        updateUiState {
            copy(searchString = text)
        }
    }

    private fun toggleProgramVisibility(name: String, programId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val programs = if (programId.isNotBlank()) {
                getChannelsEpgUseCase(channelId = programId)
            } else {
                emptyList()
            }

            updateUiState {
                copy(
                    selectedName = name,
                    selectedPrograms = programs
                )
            }
        }
    }

    private fun viewTypeChange(type: ChannelsViewType) {
        if (uiState.value.viewType != type) {
            viewModelScope.launch {
                preferencesStore.update { preferences ->
                    preferences.copy(channelsViewType = type.name)
                }
                updateUiState {
                    copy(viewType = type)
                }
            }
        }
    }

    private fun toggleFavorites(
        channel: TvChannelWithPrograms,
        type: FavoriteType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedChannel = channel.toggleFavorite(type = type.name)

            val updatedChannels = uiState.value.channels
                .replaceUpdated(channel = updatedChannel)

            updateUiState {
                copy(channels = updatedChannels, osdType = null)
            }

            toggleFavoriteChannelUseCase(channel = channel.channel, type = type.name)
        }
    }
}

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
    data class OpenOsd(val type: GroupChannelsOSD) : GroupChannelsUiAction
    data object CloseOsd : GroupChannelsUiAction
}

sealed interface GroupChannelsUiEffect {
    data class OnNavigateToPlayer(
        val name: String,
        val group: String,
        val groupType: String
    ) : GroupChannelsUiEffect

    data object OnNavigateBack : GroupChannelsUiEffect
}
