/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.usecases.EpgInfoUpdateUseCase
import com.mvproject.tinyiptv.data.usecases.GetRemotePlaylistsUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateChannelsEpgInfoUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateEpgUseCase
import com.mvproject.tinyiptv.data.usecases.UpdateRemotePlaylistChannelsUseCase
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import com.mvproject.tinyiptv.utils.typeToDuration
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val getRemotePlaylistsUseCase: GetRemotePlaylistsUseCase,
    private val updateChannelsEpgInfoUseCase: UpdateChannelsEpgInfoUseCase,
    private val epgInfoUpdateUseCase: EpgInfoUpdateUseCase,
    private val updateEpgUseCase: UpdateEpgUseCase,
    private val updateRemotePlaylistChannelsUseCase: UpdateRemotePlaylistChannelsUseCase

) : ViewModel() {

    fun checkUpdates() {
        viewModelScope.launch {
            preferenceRepository.isChannelsEpgInfoUpdateRequired()
                .collect { isRequired ->
                    if (isRequired) {
                        updateChannelsEpgInfoUseCase()
                    }
                }
        }

        viewModelScope.launch {
            preferenceRepository.isEpgUpdateRequired()
                .collect { isRequired ->
                    if (isRequired) {
                        updateEpgUseCase()
                    }
                }
        }

        viewModelScope.launch {
            val isEpgInfoDataUpdateRequired = preferenceRepository.isEpgInfoDataUpdateRequired()
            if (isEpgInfoDataUpdateRequired) {
                epgInfoUpdateUseCase()
            }
        }

        viewModelScope.launch {
            val remotePlaylists = getRemotePlaylistsUseCase()

            remotePlaylists.forEach { playlist ->
                val updateDuration = typeToDuration(playlist.updatePeriod.toInt())
                val isRequiredUpdate = actualDate - playlist.lastUpdateDate > updateDuration
                Napier.w("testing remotePlaylists ${playlist.playlistTitle} isRequiredUpdate $isRequiredUpdate")
                if (isRequiredUpdate) {
                    updateRemotePlaylistChannelsUseCase(playlist = playlist)
                }
            }
        }
    }
}

