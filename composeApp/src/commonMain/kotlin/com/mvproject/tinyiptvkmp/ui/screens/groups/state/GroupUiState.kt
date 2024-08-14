package com.mvproject.tinyiptvkmp.ui.screens.groups.state

sealed interface GroupUiState {
    data object Loading : GroupUiState

    data object Groups : GroupUiState

    data object Empty : GroupUiState
}
