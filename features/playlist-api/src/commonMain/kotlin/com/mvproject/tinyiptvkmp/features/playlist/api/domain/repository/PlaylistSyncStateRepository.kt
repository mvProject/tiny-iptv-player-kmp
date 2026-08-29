package com.mvproject.tinyiptvkmp.features.playlist.api.domain.repository

interface PlaylistSyncStateRepository {
    suspend fun markChannelsEpgInfoUpdateRequired()
}
