/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 12:54
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.usecases.AddLocalPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.AddRemotePlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.GetPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.UpdatePlaylistUseCase
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptv.ui.screens.playlist.state.PlaylistState
import com.mvproject.tinyiptv.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptv.utils.getNameFromStringUri
import io.github.aakira.napier.Napier
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
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    fun setPlaylistMode(playlistId: String) {
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

            is PlaylistAction.SetLocalUri -> {
                _state.update { current ->
                    current.copy(
                        isComplete = false,
                        uri = action.uri,
                        listName = action.uri.getNameFromStringUri(),
                        localName = action.uri.getNameFromStringUri(),
                        isLocal = true,
                    )
                }
            }
        }
    }

    private fun saveLocalPlayList() {
        viewModelScope.launch {
            val result = runCatching {
                addLocalPlaylistUseCase(
                    playlist = state.value.toPlaylist(),
                    source = state.value.uri
                )
            }.onFailure {
                Napier.e("testing saveLocalPlayList failure ${it.localizedMessage}")
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
        viewModelScope.launch {
            val result = runCatching {
                addRemotePlaylistUseCase(
                    playlist = state.value.toPlaylist()
                )
            }.onFailure {
                Napier.e("testing saveRemotePlayList failure ${it.localizedMessage}")
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
        viewModelScope.launch {
            val result = runCatching {
                updatePlaylistUseCase(
                    playlist = state.value.toPlaylist()
                )
            }.onFailure {
                Napier.e("testing updatePlayList failure ${it.localizedMessage}")
            }

            _state.update { current ->
                current.copy(
                    isComplete = result.isSuccess,
                    isSaving = false
                )
            }
        }
    }
}