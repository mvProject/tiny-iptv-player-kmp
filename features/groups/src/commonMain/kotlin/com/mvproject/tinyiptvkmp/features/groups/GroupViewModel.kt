/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.features.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.base.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.features.groups.nav.GroupNavigator
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GroupViewModel(
    private val preferencesStore: ProtoStore<AppPreferencesProto>,
    private val observePlaylistsUseCase: ObservePlaylistsUseCase,
    private val selectPlaylistUseCase: SelectPlaylistUseCase,
    private val getPlaylistGroupUseCase: GetPlaylistGroupUseCase,
    private val refreshEpgChannelsUseCase: RefreshEpgChannelsUseCase,
    private val refreshEpgProgramsUseCase: RefreshEpgProgramsUseCase,
    private val updateRemotePlaylistChannelsUseCase: UpdateRemotePlaylistChannelsUseCase,
    private val updateChannelsEpgInfoUseCase: UpdateChannelsEpgInfoUseCase,
    private val cleanProgramsUseCase: CleanProgramsUseCase,
) : ViewModel(),
    KoinComponent,
    MviCore<GroupUiState, GroupUiAction, GroupUiEffect> by mviCore(GroupUiState()) {

    private val logger by injectLogger()
    private val navigator: GroupNavigator by inject()

    init {
        observePlaylistsUseCase()
            .flowOn(Dispatchers.IO)
            .onEach { playlists ->
                updateUiState {
                    copy(
                        isPlaylistSelectorVisible = playlists.count() > INT_VALUE_1,
                        playlists = playlists,
                        selectedPlaylist = playlists.firstOrNull { it.isSelected } ?: Playlist(),
                    )
                }
                refreshGroups()
            }.launchIn(viewModelScope)

        preferencesStore.data
            .map { preferences -> preferences.channelsEpgInfoUpdateRequired }
            .distinctUntilChanged()
            .onEach { isRequired ->
                logger.w { "testing isChannelsEpgInfoUpdateRequired isRequired=$isRequired" }
                if (isRequired) {
                    updateChannelsEpgInfoUseCase()
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            updateRemotePlaylistChannelsUseCase()

            // Temporarily disabled while the EPG source is unstable and the refresh pipeline is redesigned.
            // refreshEpgChannelsUseCase()
            // cleanProgramsUseCase()
            // refreshEpgProgramsUseCase(...)
        }
    }

    override fun onAction(uiAction: GroupUiAction) {
        when (uiAction) {
            GroupUiAction.RefreshPlaylist -> refresh()
            is GroupUiAction.SelectPlaylist -> {
                val selected = uiAction.playlist
                if (!selected.isSelected) {
                    viewModelScope.launch {
                        selectPlaylistUseCase(playlist = selected)
                    }
                }
            }

            is GroupUiAction.NavigateToGroup -> {
                viewModelScope.launch {
                    navigator.navigateToPlaylist(
                        title = uiAction.title,
                        group = uiAction.group
                    )
                }
            }

            GroupUiAction.NavigateToSettings -> {
                viewModelScope.launch { navigator.navigateToSettings() }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshGroups()
        }
    }

    private suspend fun refreshGroups() {
        updateUiState { copy(isLoading = true) }

        val channelGroups = getPlaylistGroupUseCase()

        val groupState = if (channelGroups.none { it.groupType == GroupType.SPECIFIED }) {
            GroupUiState.GroupState.Empty
        } else {
            GroupUiState.GroupState.Success(channelGroups)
        }

        updateUiState {
            copy(
                groupState = groupState,
                isLoading = false
            )
        }
    }
}
