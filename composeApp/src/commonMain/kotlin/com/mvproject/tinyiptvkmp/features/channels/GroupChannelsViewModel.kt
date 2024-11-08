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
import com.mvproject.tinyiptvkmp.core.common.mvi.MVI
import com.mvproject.tinyiptvkmp.core.common.mvi.mvi
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
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsContract.UiAction
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsContract.UiEffect
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsContract.UiState
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private val args = savedStateHandle.toRoute<AppRoutes.TvPlaylistChannels>()

    private val group = args.group
    private val type = args.groupType

    private var lastRefresh: Long = 0

    init {
        viewModelScope.launch {
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = type)
            val viewType = preferenceRepository.getChannelsViewType().mapViewType()

            updateUiState {
                copy(
                    viewType = viewType,
                    currentGroup = group,
                    channels = groupChannels
                )
            }
        }
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.NavigateBack -> viewModelScope.postUiEffect(UiEffect.NavigateBack)
            is UiAction.NavigateToSelected -> viewModelScope.postUiEffect(
                UiEffect.NavigateToSelected(
                    name = uiAction.name,
                    group = uiAction.group
                )
            )

            is UiAction.SearchTextChange -> searchTextChange(text = uiAction.text)
            is UiAction.ToggleEpgVisibility -> toggleEpgVisibility(
                name = uiAction.name,
                epgId = uiAction.epgID
            )

            is UiAction.ToggleFavourites -> toggleFavorites(
                channel = uiAction.channel,
                type = uiAction.type
            )

            is UiAction.ViewTypeChange -> viewTypeChange(type = uiAction.type)
        }
    }

    fun loadChannelsByGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgPrograms()
        }
    }

    private suspend fun refreshEpgPrograms() {
        if (TimeUtils.actualDate - lastRefresh > 1.minutes.inWholeMilliseconds) {
            val channels = uiState.value.channels
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

                updateUiState {
                    copy(channels = channelsWithPrograms)
                }

                lastRefresh = TimeUtils.actualDate
            }
        }
    }

    private fun searchTextChange(text: String) {
        updateUiState {
            copy(searchString = text)
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
            Logger.w("testing toggleFavorites type $type")

            val channelWithEpg = channel.toggleFavorite(type = type)

            val updatedChannels = uiState.value.channels
                .replaceUpdated(channel = channelWithEpg)

            updateUiState {
                copy(channels = updatedChannels)
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
