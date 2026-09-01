/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 12:57
 *
 */

package com.mvproject.tinyiptvkmp.features.groups

import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.core.base.mvi.runCatchingSuspend
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_VALUE_1
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ObserveChannelsEpgInfoUpdateRequiredUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.CleanProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgChannelsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.RefreshEpgProgramsUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelsGroup
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.GroupType
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetPlaylistGroupUseCase
import com.mvproject.tinyiptvkmp.features.groups.nav.GroupNavigator
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.ObservePlaylistsUseCase
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.SelectPlaylistUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.inject

class GroupViewModel(
    private val observeChannelsEpgInfoUpdateRequiredUseCase: ObserveChannelsEpgInfoUpdateRequiredUseCase,
    private val observePlaylistsUseCase: ObservePlaylistsUseCase,
    private val selectPlaylistUseCase: SelectPlaylistUseCase,
    private val getPlaylistGroupUseCase: GetPlaylistGroupUseCase,
    private val refreshEpgChannelsUseCase: RefreshEpgChannelsUseCase,
    private val refreshEpgProgramsUseCase: RefreshEpgProgramsUseCase,
    private val updateChannelsEpgInfoUseCase: UpdateChannelsEpgInfoUseCase,
    private val cleanProgramsUseCase: CleanProgramsUseCase,
) : MviViewModel<GroupState, GroupAction, GroupEffect>() {

    private val navigator: GroupNavigator by inject()

    override fun createStore() = createStore(
        initialState = GroupState(),
        invokeOnStart = { listenPlaylists() },
    )

    override fun onIntent(intent: GroupAction) {
        when (intent) {
            is GroupAction.NavigateToGroup -> launch {
                navigator.navigateToPlaylist(
                    playlistId = getState().selectedPlaylist.id,
                    title = intent.title,
                    group = intent.group
                )
            }

            GroupAction.NavigateToSettings -> launch { navigator.navigateToSettings() }
            is GroupAction.SelectPlaylist -> launch { selectPlaylist(playlistId = intent.playlistId) }
        }
    }

    private suspend fun listenPlaylists() {
        observePlaylistsUseCase()
            .distinctUntilChanged()
            .collect { playlists ->
                val selectedPlaylist = playlists.firstOrNull { playlist -> playlist.isSelected }
                setState {
                    copy(
                        isPlaylistSelectorVisible = playlists.size > INT_VALUE_1,
                        playlists = playlists,
                        selectedPlaylist = selectedPlaylist ?: Playlist(),
                        groupState = if (selectedPlaylist == null) {
                            GroupState.GroupState.Empty
                        } else {
                            groupState
                        },
                        isLoading = if (selectedPlaylist == null) false else isLoading,
                    )
                }
                if (selectedPlaylist != null) {
                    refreshGroups()
                }
            }
    }

    // Temporarily disabled while the EPG source is unstable and the refresh pipeline is redesigned.
    private suspend fun listenUpdates() {
        observeChannelsEpgInfoUpdateRequiredUseCase()
            .distinctUntilChanged()
            .onEach { isRequired ->
                logger.w { "testing isChannelsEpgInfoUpdateRequired isRequired=$isRequired" }
                if (isRequired) {
                    updateChannelsEpgInfoUseCase()
                }
            }.launchIn(viewModelScope)
        // Temporarily disabled while the EPG source is unstable and the refresh pipeline is redesigned.
        // refreshEpgChannelsUseCase()
        // cleanProgramsUseCase()
        // refreshEpgProgramsUseCase(...)
    }

    private suspend fun selectPlaylist(playlistId: String) {
        if (playlistId != getState().selectedPlaylist.id) {
            selectPlaylistUseCase(playlistId = playlistId)
        }
    }

    private suspend fun refreshGroups() {
        setState { copy(isLoading = true) }

        runCatchingSuspend {
            getPlaylistGroupUseCase(playlistId = getState().selectedPlaylist.id)
        }.onSuccess { channelGroups ->
            setState {
                copy(
                    groupState = channelGroups.toGroupState(),
                    isLoading = false
                )
            }
        }.onFailure { throwable ->
            logger.e(throwable) { "Failed to refresh playlist groups" }
            setState { copy(isLoading = false) }
        }
    }

    private fun List<ChannelsGroup>.toGroupState(): GroupState.GroupState =
        if (none { group -> group.groupType == GroupType.SPECIFIED }) {
            GroupState.GroupState.Empty
        } else {
            GroupState.GroupState.Success(this)
        }
}
