/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.usecases.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptvkmp.ui.screens.channels.navigation.TvPlaylistChannelsArgs
import com.mvproject.tinyiptvkmp.ui.screens.channels.state.TvPlaylistGroupState
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class TvPlaylistChannelsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    private val _groupState = MutableStateFlow(TvPlaylistGroupState())
    val groupState = _groupState.asStateFlow()

    private val args = TvPlaylistChannelsArgs(savedStateHandle)
    private val group = args.group
    private val type = args.type

    private var lastRefresh: Long = 0

    init {
        viewModelScope.launch {
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = type)

            val viewType =
                preferenceRepository
                    .getChannelsViewType()
                    ?.let { ChannelsViewType.valueOf(it) }
                    ?: ChannelsViewType.LIST

            _groupState.update { state ->
                state.copy(
                    viewType = viewType,
                    currentGroup = group,
                    channels = groupChannels
                )
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
            val channels = groupState.value.channels
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

                _groupState.update { state ->
                    state.copy(channels = channelsWithPrograms)
                }

                lastRefresh = TimeUtils.actualDate
            }
        }
    }


    fun processAction(action: TvPlaylistChannelAction) {
        when (action) {
            is TvPlaylistChannelAction.SearchTextChange -> {
                searchTextChange(text = action.text)
            }

            TvPlaylistChannelAction.SearchTriggered -> {
                searchTriggered()
            }

            is TvPlaylistChannelAction.ToggleEpgVisibility -> {
                toggleEpgVisibility(name = action.name, epgId = action.epgID)
            }

            is TvPlaylistChannelAction.ToggleFavourites -> {
                toggleFavorites(channel = action.channel, type = action.type)
            }

            is TvPlaylistChannelAction.ViewTypeChange -> {
                viewTypeChange(type = action.type)
            }
        }
    }

    private fun searchTextChange(text: String) {
        _groupState.update { current ->
            current.copy(searchString = text)
        }
    }

    private fun searchTriggered() {
        _groupState.update { state ->
            state.copy(isSearching = !state.isSearching)
        }
    }

    private fun toggleEpgVisibility(name: String, epgId: String) {
        KLog.w("testing name:$name,epgId:$epgId")
        viewModelScope.launch(Dispatchers.IO) {
            val programs = if (epgId.isNotBlank()) {
                getChannelsEpgUseCase(channelId = epgId)
            } else {
                emptyList()
            }
            _groupState.update { state ->
                state.copy(
                    selectedName = name,
                    selectedPrograms = programs
                )
            }
        }
    }

    private fun viewTypeChange(type: ChannelsViewType) {
        if (groupState.value.viewType != type) {
            viewModelScope.launch {
                _groupState.update { current ->
                    current.copy(viewType = type)
                }
                preferenceRepository.setChannelsViewType(type = type.name)
            }
        }
    }

    private fun toggleFavorites(
        channel: TvPlaylistChannel,
        type: FavoriteType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            KLog.w("testing toggleFavorites type $type")

            val channelWithEpg = channel.toggleFavorite(
                type = type
            )

            val updatedChannels = groupState.value.channels.replaceUpdated(
                channel = channelWithEpg,
            )

            _groupState.update { state ->
                state.copy(channels = updatedChannels)
            }

            toggleFavoriteChannelUseCase(
                channel = channel,
                favoriteType = channelWithEpg.favoriteType
            )
        }
    }

    private fun TvPlaylistChannel.toggleFavorite(
        type: FavoriteType
    ): TvPlaylistChannel {
        val favType =
            if (this.favoriteType == type) {
                FavoriteType.NONE
            } else {
                type
            }
        return this.copy(favoriteType = favType)
    }

    private fun List<TvPlaylistChannel>.replaceUpdated(
        channel: TvPlaylistChannel
    ): List<TvPlaylistChannel> {
        val index = this.indexOfFirst { it.channelName == channel.channelName }
        return this.toMutableList().apply { set(index, channel) }
    }
}
