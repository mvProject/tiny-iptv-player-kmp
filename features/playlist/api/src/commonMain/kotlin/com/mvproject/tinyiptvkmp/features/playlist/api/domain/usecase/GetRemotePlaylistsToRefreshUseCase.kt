package com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase

import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository.PlaylistRepository
import kotlin.time.Clock

interface GetRemotePlaylistsToRefreshUseCase {
    suspend operator fun invoke(
        nowMillis: Long = Clock.System.now().toEpochMilliseconds()
    ): List<Playlist>
}

internal class GetRemotePlaylistsToRefreshUseCaseImpl(
    private val playlistRepository: PlaylistRepository,
) : GetRemotePlaylistsToRefreshUseCase {
    override suspend operator fun invoke(nowMillis: Long): List<Playlist> =
        playlistRepository.getRemotePlaylistsToRefresh(nowMillis = nowMillis)
}
