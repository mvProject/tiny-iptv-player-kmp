package com.mvproject.tinyiptvkmp.features.playlist.navigation

import androidx.compose.runtime.Composable
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistDetailArgs
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistScreen
import com.mvproject.tinyiptvkmp.features.playlist.PlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlaylistDetailRoute(
    playlistId: String,
    onNavigateBack: () -> Unit,
) {
    val playlistViewModel =
        koinViewModel<PlaylistViewModel>(
            parameters = { parametersOf(PlaylistDetailArgs(playlistId = playlistId)) },
        )

    PlaylistScreen(
        viewModel = playlistViewModel,
        onNavigateBack = onNavigateBack,
    )
}
