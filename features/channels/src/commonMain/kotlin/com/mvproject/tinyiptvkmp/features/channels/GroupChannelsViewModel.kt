/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType.Companion.mapViewType
import com.mvproject.tinyiptvkmp.core.foundation.utils.actualDate
import com.mvproject.tinyiptvkmp.features.channels.GroupChannelsUiState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.channels.nav.GroupChannelsNavigator
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    @InjectedParam args: GroupChannelsArgs,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
) : ViewModel(),
    KoinComponent,
    MviCore<GroupChannelsUiState, GroupChannelsUiAction, GroupChannelsUiEffect> by mviCore(
        GroupChannelsUiState()
    ) {

    private val group = args.group
    private val type = args.groupType

    private var lastRefresh: Long = 0

    private val navigator: GroupChannelsNavigator by inject()


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
            GroupChannelsUiAction.NavigateBack -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }

            is GroupChannelsUiAction.SelectChannel -> {
                viewModelScope.launch {
                    navigator.navigateToPlayer(
                        name = uiAction.name,
                        group = uiAction.group,
                        groupType = type
                    )
                }
            }

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


