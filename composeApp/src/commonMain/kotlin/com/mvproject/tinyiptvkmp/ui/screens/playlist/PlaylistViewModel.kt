/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 06.05.24, 20:00
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetPlaylistUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.SavePlaylistUseCase
import com.mvproject.tinyiptvkmp.data.enums.PlaylistType
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
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
    savedStateHandle: SavedStateHandle,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val savePlaylistUseCase: SavePlaylistUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(PlaylistState())
    val state = _state.asStateFlow()

    private val args = savedStateHandle.toRoute<AppRoutes.PlaylistDetail>()

    init {
        initPlaylist(playlistId = args.id)
    }

    private fun initPlaylist(playlistId: String) {
        viewModelScope.launch {
            val playlist = getPlaylistUseCase(playlistId = playlistId)

            _state.update { current ->
                current.copy(
                    selectedId = playlist.id,
                    playlistName = playlist.playlistName,
                    playlistSource = playlist.playlistSource,
                    playlistType = playlist.playlistType,
                    isEdit = playlist.id != LONG_VALUE_ZERO,
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

                saveOrUpdatePlayList()
            }

            PlaylistAction.UpdatePlaylist -> {
                _state.update { current ->
                    current.copy(isSaving = true)
                }
                saveOrUpdatePlayList(isUpdate = true)
            }

            is PlaylistAction.SetTitle -> {
                _state.update { current ->
                    current.copy(playlistName = action.title)
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
                        playlistSource = action.url,
                        playlistType = PlaylistType.REMOTE,
                    )
                }
            }

            is PlaylistAction.SetLocalUri -> {
                _state.update { current ->
                    current.copy(
                        playlistName = action.name,
                        playlistSource = action.uri,
                        playlistType = PlaylistType.LOCAL,
                    )
                }
            }
        }
    }

    private fun saveOrUpdatePlayList(isUpdate: Boolean = false) {
        viewModelScope.launch {
            val playlist = state.value.toPlaylist()

            val result =
                runCatching {
                    savePlaylistUseCase(playlist = playlist, isUpdate = isUpdate)
                }.onFailure {
                    KLog.e("testing saveOrUpdatePlayList isUpdate=$isUpdate, failure ${it.localizedMessage}")
                }

            _state.update { current ->
                current.copy(isComplete = result.isSuccess, isSaving = false)
            }
        }
    }
}
