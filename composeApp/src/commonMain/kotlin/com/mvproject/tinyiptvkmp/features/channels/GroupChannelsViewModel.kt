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
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.common.utils.actualDate
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType.Companion.mapViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.core.domain.utils.mapProgramIds
import com.mvproject.tinyiptvkmp.core.domain.utils.mapPrograms
import com.mvproject.tinyiptvkmp.core.domain.utils.replaceUpdated
import com.mvproject.tinyiptvkmp.core.domain.utils.toggleFavorite
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsUiState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    @InjectedParam args: AppRoutes.TvPlaylistChannels,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferenceRepository: PreferenceRepository,
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
            val viewType = preferenceRepository.getChannelsViewType().mapViewType()
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = type)
            updateUiState {
                copy(
                    viewType = viewType,
                    currentGroup = group,
                    channels = groupChannels
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
                preferenceRepository.setChannelsViewType(type = type.name)
                updateUiState {
                    copy(viewType = type)
                }
            }
        }
    }

    private fun toggleFavorites(
        channel: TvChannel,
        type: FavoriteType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedChannel = channel.toggleFavorite(type = type)

            val updatedChannels = uiState.value.channels
                .replaceUpdated(channel = updatedChannel)

            updateUiState {
                copy(channels = updatedChannels, osdType = null)
            }

            toggleFavoriteChannelUseCase(channel = channel, type = type)
        }
    }
}

@Immutable
data class GroupChannelsUiState(
    val currentGroup: String = String.empty,
    val isLoading: Boolean = false,
    val searchString: String = String.empty,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
    val channels: List<TvChannel> = emptyList(),
    val selectedName: String = String.empty,
    val selectedPrograms: List<EpgProgram> = emptyList(),
    val osdType: GroupChannelsOSD? = null
) {
    sealed interface GroupChannelsOSD {
        data class ChannelPrograms(val channel: TvChannel) : GroupChannelsOSD
        data class ChannelFavorites(val channel: TvChannel) : GroupChannelsOSD
    }
}

sealed interface GroupChannelsUiAction {
    data class ToggleFavorite(
        val channel: TvChannel,
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
