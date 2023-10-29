/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 21:25
 *
 */

package com.mvproject.tinyiptv.ui.screens.channels

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.helpers.ViewTypeHelper
import com.mvproject.tinyiptv.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptv.data.usecases.ToggleChannelEpgUseCase
import com.mvproject.tinyiptv.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptv.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptv.ui.screens.channels.state.TvPlaylistChannelState
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TvPlaylistChannelsViewModel(
    private val viewTypeHelper: ViewTypeHelper,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val toggleChannelEpgUseCase: ToggleChannelEpgUseCase

) : ScreenModel {
    private val _viewState = MutableStateFlow(TvPlaylistChannelState())
    val viewState = _viewState.asStateFlow()

    val channels = mutableStateListOf<TvPlaylistChannel>()

    private val _searchText = MutableStateFlow(EMPTY_STRING)

    init {
        coroutineScope.launch {
            _viewState.update {
                it.copy(
                    viewType = viewTypeHelper.getChannelsViewType()
                )
            }
        }
    }

    fun loadChannelsByGroups(group: String) {
        _viewState.update {
            it.copy(currentGroup = group, isLoading = true)
        }
        coroutineScope.launch(Dispatchers.IO) {
            val groupChannels = getGroupChannelsUseCase(group)
            channels.apply {
                clear()
                addAll(groupChannels)
            }

            _viewState.update {
                it.copy(isLoading = false)
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
        _viewState.update {
            it.copy(searchString = text)
        }
    }

    private fun viewTypeChange(type: ChannelsViewType) {
        if (viewState.value.viewType != type) {
            coroutineScope.launch {
                _viewState.update {
                    it.copy(viewType = type)
                }
                viewTypeHelper.setChannelsViewType(type)
            }
        }
    }

    private fun searchTriggered() {
        val searchState = viewState.value.isSearching
        _viewState.update {
            it.copy(isSearching = !searchState)
        }
    }

    private fun toggleFavorites(channel: TvPlaylistChannel) {
        val isFavorite = channel.isInFavorites
        coroutineScope.launch {

            channels.set(
                index = channels.indexOf(channel),
                element = channel.copy(isInFavorites = !isFavorite)
            )

            toggleFavoriteChannelUseCase(channel = channel)

        }
    }

    private fun toggleEpgState(channel: TvPlaylistChannel) {
        val isEpgUsing = channel.isEpgUsing
        coroutineScope.launch {

            channels.set(
                index = channels.indexOf(channel),
                element = channel.copy(isEpgUsing = !isEpgUsing)
            )

            toggleChannelEpgUseCase(channel = channel)

        }
    }

    private fun toggleEpgVisibility() {
        val epgCurrentState = viewState.value.isEpgVisible
        _viewState.update {
            it.copy(isEpgVisible = !epgCurrentState)
        }
    }
}