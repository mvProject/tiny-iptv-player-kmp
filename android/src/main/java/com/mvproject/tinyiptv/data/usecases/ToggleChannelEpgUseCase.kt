/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.SelectedEpg
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.repository.SelectedEpgRepository

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