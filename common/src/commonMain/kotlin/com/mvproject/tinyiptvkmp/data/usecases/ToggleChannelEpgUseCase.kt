/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.usecases

import com.mvproject.tinyiptvkmp.data.model.channels.TvPlaylistChannel
import com.mvproject.tinyiptvkmp.data.model.epg.SelectedEpg
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.data.repository.SelectedEpgRepository

class ToggleChannelEpgUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val selectedEpgRepository: SelectedEpgRepository
) {
    suspend operator fun invoke(
        channel: TvPlaylistChannel
    ) {
        val isEpgUsing = channel.isEpgUsing

        if (isEpgUsing) {
            selectedEpgRepository.deleteSelectedEpg(
                id = channel.epgId
            )

        } else {
            selectedEpgRepository.addSelectedEpg(
                SelectedEpg(
                    channelName = channel.channelName,
                    channelEpgId = channel.epgId
                )
            )

            preferenceRepository.setEpgUnplannedUpdateRequired(state = true)
        }
    }
}