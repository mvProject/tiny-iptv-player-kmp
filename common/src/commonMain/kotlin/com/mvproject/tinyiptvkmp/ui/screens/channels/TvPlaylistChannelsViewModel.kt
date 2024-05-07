/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 11:33
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.channels

import androidx.compose.runtime.mutableStateListOf
import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.helpers.ViewTypeHelper
import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleChannelEpgUseCase
import com.mvproject.tinyiptvkmp.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelGroup
import com.mvproject.tinyiptvkmp.ui.screens.channels.state.TvPlaylistChannelState
import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class TvPlaylistChannelsViewModel(
    private val viewTypeHelper: ViewTypeHelper,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val toggleChannelEpgUseCase: ToggleChannelEpgUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(TvPlaylistChannelState())
    val viewState = _viewState.asStateFlow()

    val channels = mutableStateListOf<TvPlaylistChannel>()

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

    fun loadChannelsByGroups(group: String) {
        _viewState.update { current ->
            current.copy(currentGroup = group, isLoading = true)
        }
        viewModelScope.launch {
            val groupChannels = getGroupChannelsUseCase(group)
            channels.apply {
                clear()
                addAll(groupChannels)
            }
            _channelsState.value = TvPlaylistChannelGroup(items = groupChannels)

            _viewState.update { current ->
                current.copy(isLoading = false)
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

            is TvPlaylistChannelAction.ToggleEpgState -> {
                toggleEpgState(channel = action.channel)
            }

            TvPlaylistChannelAction.ToggleEpgVisibility -> {
                toggleEpgVisibility()
            }

            is TvPlaylistChannelAction.ToggleFavourites -> {
                toggleFavorites(channel = action.channel)
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
        val searchState = viewState.value.isSearching
        _viewState.update { current ->
            current.copy(isSearching = !searchState)
        }
    }

    private fun toggleFavorites(channel: TvPlaylistChannel) {
        val isFavorite = channel.isInFavorites
        viewModelScope.launch {
            channels.set(
                index = channels.indexOf(channel),
                element = channel.copy(isInFavorites = !isFavorite),
            )
            toggleFavoriteChannelUseCase(channel = channel)
        }
    }

    private fun toggleEpgState(channel: TvPlaylistChannel) {
        val isEpgUsing = channel.isEpgUsing
        viewModelScope.launch {
            channels.set(
                index = channels.indexOf(channel),
                element = channel.copy(isEpgUsing = !isEpgUsing),
            )
            toggleChannelEpgUseCase(channel = channel)
        }
    }

    private fun toggleEpgVisibility() {
        val epgCurrentState = viewState.value.isEpgVisible
        _viewState.update { current ->
            current.copy(isEpgVisible = !epgCurrentState)
        }
    }
}
