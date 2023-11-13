/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 27.10.23, 21:28
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mvproject.tinyiptv.data.usecases.AddLocalPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.AddRemotePlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.GetPlaylistUseCase
import com.mvproject.tinyiptv.data.usecases.UpdatePlaylistUseCase
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptv.ui.screens.playlist.state.PlaylistState
import com.mvproject.tinyiptv.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptv.utils.CommonUtils.getNameFromStringUri
import com.mvproject.tinyiptv.utils.KLog
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
        coroutineScope.launch {
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
        coroutineScope.launch {
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
        coroutineScope.launch {
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
        coroutineScope.launch {
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
}