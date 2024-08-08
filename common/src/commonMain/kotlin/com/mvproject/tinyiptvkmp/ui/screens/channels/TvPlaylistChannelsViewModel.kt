/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels

import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.enums.FavoriteType
import com.mvproject.tinyiptvkmp.data.helpers.ViewTypeHelper
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsEpg
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.ChannelEpg
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelGroup
import com.mvproject.tinyiptvkmp.ui.screens.channels.state.TvPlaylistChannelState
import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class TvPlaylistChannelsViewModel(
    private val viewTypeHelper: ViewTypeHelper,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpg: GetGroupChannelsEpg,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(TvPlaylistChannelState())
    val viewState = _viewState.asStateFlow()

    private val _channelsState = MutableStateFlow(TvPlaylistChannelGroup())
    val channelsState = _channelsState.asStateFlow()

    private val _searchText = MutableStateFlow(EMPTY_STRING)

    init {
        viewModelScope.launch {
            _viewState.update { current ->
                current.copy(
                    viewType = viewTypeHelper.getChannelsViewType(),
                )
            }
        }
    }

    fun loadChannelsByGroups(
        group: String,
        groupType: String,
    ) {
        _viewState.update { current ->
            current.copy(currentGroup = group, isLoading = true)
        }
        viewModelScope.launch {
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = groupType)

            _channelsState.value = TvPlaylistChannelGroup(items = groupChannels)

            _viewState.update { current ->
                current.copy(isLoading = false)
            }

            launchEpgUpdate()
        }
    }

    private suspend fun launchEpgUpdate() {
        val channels = channelsState.value.items
        if (channels.isNotEmpty()) {
            val channelsData =
                channels
                    .filter { it.epgId.isNotBlank() }
                    .map {
                        ChannelEpg(
                            channelName = it.channelName,
                            channelEpgId = it.epgId,
                        )
                    }

            val channelsEpgData = getGroupChannelsEpg(channels = channelsData)

            channelsEpgData.forEach { data ->
                delay(200)
                applyEpg(data = data)
            }
        }
    }

    private fun applyEpg(data: ChannelEpg) {
        val channels = channelsState.value.items
        val channelIndex = channels.indexOfFirst { it.channelName == data.channelName }

        val channelWithEpg =
            updateChannelWithEpg(
                index = channelIndex,
                programsData = data.programs,
            )

        updateChannel(
            index = channelIndex,
            channel = channelWithEpg,
        )
    }

    private fun updateChannelWithEpg(
        index: Int,
        programsData: List<EpgProgram>,
    ): TvPlaylistChannel {
        val current = channelsState.value.items[index]
        val programs = TvPlaylistChannelEpg(items = programsData)
        val updated = current.copy(channelEpg = programs)
        return updated
    }

    private fun updateChannel(
        index: Int,
        channel: TvPlaylistChannel,
    ) {
        val current = channelsState.value.items

        val updatedList =
            current
                .toMutableList()
                .apply {
                    set(index, channel)
                }

        _channelsState.update { state ->
            state.copy(items = updatedList)
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

            TvPlaylistChannelAction.ToggleEpgVisibility -> {
                toggleEpgVisibility()
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
        _searchText.value = text
        _viewState.update { current ->
            current.copy(searchString = text)
        }
    }

    private fun viewTypeChange(type: ChannelsViewType) {
        if (viewState.value.viewType != type) {
            viewModelScope.launch {
                _viewState.update { current ->
                    current.copy(viewType = type)
                }
                viewTypeHelper.setChannelsViewType(type)
            }
        }
    }

    private fun searchTriggered() {
        _viewState.update { current ->
            val searchState = viewState.value.isSearching
            current.copy(isSearching = !searchState)
        }
    }

    private fun toggleFavorites(
        channel: TvPlaylistChannel,
        type: FavoriteType,
    ) {
        viewModelScope.launch {
            KLog.w("testing toggleFavorites type $type")

            val channels = channelsState.value.items
            val channelIndex = channels.indexOfFirst { it.channelName == channel.channelName }

            val favType =
                if (channel.favoriteType == type) {
                    FavoriteType.NONE
                } else {
                    type
                }

            val channelWithEpg =
                channel.copy(favoriteType = favType)

            updateChannel(
                index = channelIndex,
                channel = channelWithEpg,
            )

            toggleFavoriteChannelUseCase(channel = channel, favoriteType = favType)
        }
    }

    private fun toggleEpgVisibility() {
        _viewState.update { current ->
            val epgCurrentState = current.isEpgVisible
            current.copy(isEpgVisible = !epgCurrentState)
        }
    }
}
