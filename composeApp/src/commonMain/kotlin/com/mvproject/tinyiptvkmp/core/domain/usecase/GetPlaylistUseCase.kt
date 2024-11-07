package com.mvproject.tinyiptvkmp.core.domain.usecase

import com.mvproject.tinyiptvkmp.core.data.repository.PlaylistsRepository
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist
import com.mvproject.tinyiptvkmp.utils.AppConstants

class GetPlaylistUseCase(
    private val playlistsRepository: PlaylistsRepository,
) {
    suspend operator fun invoke(playlistId: String): Playlist {
        val id = playlistId.toLongOrNull() ?: return Playlist(id = AppConstants.LONG_VALUE_ZERO)
        return playlistsRepository.getPlaylistById(id = id)
    }
}