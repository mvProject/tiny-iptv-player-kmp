package com.mvproject.tinyiptvkmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.runCatchingSuspend
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.usecase.PlaylistContentCoordinator
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class RootViewModel(
    private val playlistContentCoordinator: PlaylistContentCoordinator,
) : ViewModel(), KoinComponent {
    private val logger by injectLogger()

    fun refreshRemotePlaylistContentOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingSuspend {
                playlistContentCoordinator.refreshRemotePlaylistContent()
            }.onFailure { throwable ->
                logger.e(throwable) { "Failed to refresh remote playlist content" }
            }
        }
    }
}
