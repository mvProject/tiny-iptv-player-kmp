/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType.Companion.mapViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.channels.action.GroupChannelsAction
import com.mvproject.tinyiptvkmp.features.channels.state.GroupChannelsState
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    private val _groupChannelsState = MutableStateFlow(GroupChannelsState())
    val groupChannelsState: StateFlow<GroupChannelsState> = _groupChannelsState

    private val args = savedStateHandle.toRoute<AppRoutes.TvPlaylistChannels>()

    private val group = args.group
    private val type = args.groupType

    private var lastRefresh: Long = 0

    init {
        viewModelScope.launch {
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = type)

            val viewType = preferenceRepository.getChannelsViewType().mapViewType()

            _groupChannelsState.update { state ->
                state.copy(
                    viewType = viewType,
                    currentGroup = group,
                    channels = groupChannels
                )
            }
        }
    }

    fun processAction(action: GroupChannelsAction) {
        when (action) {
            is GroupChannelsAction.SearchTextChange -> {
                searchTextChange(text = action.text)
            }

            is GroupChannelsAction.ToggleEpgVisibility -> {
                toggleEpgVisibility(name = action.name, epgId = action.epgID)
            }

            is GroupChannelsAction.ToggleFavourites -> {
                toggleFavorites(channel = action.channel, type = action.type)
            }

            is GroupChannelsAction.ViewTypeChange -> {
                viewTypeChange(type = action.type)
            }
        }
    }


    fun loadChannelsByGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgPrograms()
        }
    }

    private suspend fun refreshEpgPrograms() {
        if (TimeUtils.actualDate - lastRefresh > 1.minutes.inWholeMilliseconds) {
            val channels = groupChannelsState.value.channels
            if (channels.isNotEmpty()) {
                val channelsIds =
                    channels
                        .map { it.epgId }
                        .filter { it.isNotBlank() }

                val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)

                val channelsWithPrograms = channels.map { ch ->
                    val programs = channelsEpgData[ch.epgId] ?: emptyList()
                    ch.copy(programs = programs)
                }

                _groupChannelsState.update { state ->
                    state.copy(channels = channelsWithPrograms)
                }

                lastRefresh = TimeUtils.actualDate
            }
        }
    }

    private fun searchTextChange(text: String) {
        _groupChannelsState.update { current ->
            current.copy(searchString = text)
        }
    }

    private fun toggleEpgVisibility(name: String, epgId: String) {
        Logger.w("testing name:$name,epgId:$epgId")
        viewModelScope.launch(Dispatchers.IO) {
            val programs = if (epgId.isNotBlank()) {
                getChannelsEpgUseCase(channelId = epgId)
            } else {
                emptyList()
            }
            _groupChannelsState.update { state ->
                state.copy(
                    selectedName = name,
                    selectedPrograms = programs
                )
            }
        }
    }

    private fun viewTypeChange(type: ChannelsViewType) {
        if (groupChannelsState.value.viewType != type) {
            viewModelScope.launch {
                _groupChannelsState.update { current ->
                    current.copy(viewType = type)
                }
                preferenceRepository.setChannelsViewType(type = type.name)
            }
        }
    }

    private fun toggleFavorites(
        channel: TvChannel,
        type: FavoriteType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Logger.w("testing toggleFavorites type $type")

            val channelWithEpg = channel.toggleFavorite(type = type)

            val updatedChannels = groupChannelsState.value.channels
                .replaceUpdated(channel = channelWithEpg)

            _groupChannelsState.update { state ->
                state.copy(channels = updatedChannels)
            }

            toggleFavoriteChannelUseCase(
                channel = channel,
                favoriteType = channelWithEpg.favoriteType
            )
        }
    }

    private fun TvChannel.toggleFavorite(
        type: FavoriteType
    ): TvChannel {
        val favType =
            if (this.favoriteType == type) {
                FavoriteType.NONE
            } else {
                type
            }
        return this.copy(favoriteType = favType)
    }

    private fun List<TvChannel>.replaceUpdated(
        channel: TvChannel
    ): List<TvChannel> {
        val index = this.indexOfFirst { it.channelName == channel.channelName }
        return this.toMutableList().apply { set(index, channel) }
    }
}
