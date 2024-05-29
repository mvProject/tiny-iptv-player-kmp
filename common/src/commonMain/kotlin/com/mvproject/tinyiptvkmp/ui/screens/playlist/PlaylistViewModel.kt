/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist

import com.mvproject.tinyiptvkmp.data.usecases.AddLocalPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.AddRemotePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.data.usecases.UpdatePlaylistUseCase
import com.mvproject.tinyiptvkmp.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptvkmp.ui.screens.playlist.state.PlaylistState
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.KLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import kotlin.random.Random

class PlaylistViewModel(
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val addRemotePlaylistUseCase: AddRemotePlaylistUseCase,
    private val addLocalPlaylistUseCase: AddLocalPlaylistUseCase,
    // private val saveRemotePlaylistChannels: SaveRemotePlaylistChannels,
    // private val saveLocalPlaylistChannels: SaveLocalPlaylistChannels,
    private val getPlaylistUseCase: GetPlaylistUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    fun setPlaylistMode(playlistId: String) {
        KLog.w("testing PlaylistViewModel playlistId:$playlistId")
        viewModelScope.launch {
            val playlist = getPlaylistUseCase(playlistId = playlistId)
            _state.update { current ->
                current.copy(
                    selectedId = playlist.id,
                    isEdit = playlist.id != LONG_VALUE_ZERO,
                    listName = playlist.playlistTitle,
                    localName = playlist.playlistLocalName,
                    isLocal = playlist.isLocalSource,
                    url = playlist.playlistUrl,
                    lastUpdateDate = playlist.lastUpdateDate,
                    updatePeriod = playlist.updatePeriod.toInt(),
                )
            }
        }
    }

    fun processAction(action: PlaylistAction) {
        when (action) {
            PlaylistAction.SavePlaylist -> {
                _state.update { current ->
                    current.copy(selectedId = Random.nextLong(), isSaving = true)
                }
                if (state.value.isLocal) {
                    saveLocalPlayList()
                } else {
                    saveRemotePlayList()
                }
            }

            PlaylistAction.UpdatePlaylist -> {
                _state.update { current ->
                    current.copy(isSaving = true)
                }
                updatePlayList()
            }

            is PlaylistAction.SetTitle -> {
                _state.update { current ->
                    current.copy(isComplete = false, listName = action.title)
                }
            }

            is PlaylistAction.SetUpdatePeriod -> {
                _state.update { current ->
                    current.copy(isComplete = false, updatePeriod = action.period)
                }
            }

            is PlaylistAction.SetRemoteUrl -> {
                _state.update { current ->
                    current.copy(isComplete = false, url = action.url)
                }
            }

            is PlaylistAction.SetLocalUri -> {
                _state.update { current ->
                    current.copy(
                        isComplete = false,
                        uri = action.uri,
                        listName = action.name,
                        localName = action.name,
                        isLocal = true,
                    )
                }
            }
        }
    }

    private fun saveLocalPlayList() {
        viewModelScope.launch {
            val playlist = state.value.toPlaylist()

            val result =
                runCatching {
                    addLocalPlaylistUseCase(
                        playlist = playlist,
                        source = state.value.uri,
                    )
                }.onFailure {
                    KLog.e("saveLocalPlayList failure ${it.localizedMessage}")
                }

            _state.update { current ->
                current.copy(isComplete = result.isSuccess, isSaving = false)
            }

            // runCatching {
            //    saveLocalPlaylistChannels(
            //        playlistId = playlist.id,
            //        source = state.value.uri,
            //    )
            // }.onFailure {
            //    KLog.e("saveLocalPlayList failure ${it.localizedMessage}")
            // }
        }
    }

    private fun saveRemotePlayList() {
        viewModelScope.launch {
            val playlist = state.value.toPlaylist()

            val result =
                runCatching {
                    addRemotePlaylistUseCase(playlist = playlist)
                }.onFailure {
                    KLog.e("saveRemotePlayList failure ${it.localizedMessage}")
                }

            _state.update { current ->
                current.copy(isComplete = result.isSuccess, isSaving = false)
            }

            // runCatching {
            //     saveRemotePlaylistChannels(
            //         playlistId = playlist.id,
            //         playlistUrl = playlist.playlistUrl,
            //     )
            // }.onFailure {
            //     KLog.e("saveLocalPlayList failure ${it.localizedMessage}")
            // }
        }
    }

    private fun updatePlayList() {
        viewModelScope.launch {
            val result =
                runCatching {
                    updatePlaylistUseCase(playlist = state.value.toPlaylist())
                }.onFailure {
                    KLog.e("updatePlayList failure ${it.localizedMessage}")
                }

            _state.update { current ->
                current.copy(isComplete = result.isSuccess, isSaving = false)
            }
        }
    }
}
