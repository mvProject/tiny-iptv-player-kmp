package com.mvproject.tinyiptvkmp.features.channels.api.domain.repository

interface SelectedPlaylistProvider {
    suspend fun getSelectedPlaylistId(): String
}
