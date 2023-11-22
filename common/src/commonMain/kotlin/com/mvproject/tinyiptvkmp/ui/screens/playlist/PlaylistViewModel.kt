/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
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
import kotlin.random.Random

class PlaylistViewModel(
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val addRemotePlaylistUseCase: AddRemotePlaylistUseCase,
    private val addLocalPlaylistUseCase: AddLocalPlaylistUseCase,
    private val getPlaylistUseCase: GetPlaylistUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    fun setPlaylistMode(playlistId: String) {
        screenModelScope.launch {
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
                    current.copy(
                        selectedId = Random.nextLong(),
                        isSaving = true
                    )
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
                    current.copy(
                        isComplete = false,
                        url = action.url,
                    )
                }
            }

            // todo uri fix name
            is PlaylistAction.SetLocalUri -> {
                KLog.w("testing SetLocalUri name:${action.name}, uri:${action.uri}")
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
        screenModelScope.launch {
            val result = runCatching {
                addLocalPlaylistUseCase(
                    playlist = state.value.toPlaylist(),
                    source = state.value.uri
                )
            }.onFailure {
                KLog.e("testing saveLocalPlayList failure ${it.localizedMessage}")
            }

            _state.update { current ->
                current.copy(
                    isComplete = result.isSuccess,
                    isSaving = false
                )
            }
        }
    }

    private fun saveRemotePlayList() {
        screenModelScope.launch {
            val result = runCatching {
                addRemotePlaylistUseCase(
                    playlist = state.value.toPlaylist()
                )
            }.onFailure {
                KLog.e("testing saveRemotePlayList failure ${it.localizedMessage}")
            }

            _state.update { current ->
                current.copy(
                    isComplete = result.isSuccess,
                    isSaving = false
                )
            }
        }
    }

    private fun updatePlayList() {
        screenModelScope.launch {
            val result = runCatching {
                updatePlaylistUseCase(
                    playlist = state.value.toPlaylist()
                )
            }.onFailure {
                KLog.e("testing updatePlayList failure ${it.localizedMessage}")
            }

            _state.update { current ->
                current.copy(
                    isComplete = result.isSuccess,
                    isSaving = false
                )
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        _state.update { current ->
            current.copy(isComplete = false)
        }
    }
}