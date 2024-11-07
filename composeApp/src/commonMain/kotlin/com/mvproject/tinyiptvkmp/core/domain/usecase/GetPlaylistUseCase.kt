package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.common.AppConstants
import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

class GetPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: String): Playlist {
        val id = playlistId.toLongOrNull() ?: return Playlist(id = AppConstants.LONG_VALUE_ZERO)
        return playlistsRepository.getPlaylistById(id = id)
    }
}