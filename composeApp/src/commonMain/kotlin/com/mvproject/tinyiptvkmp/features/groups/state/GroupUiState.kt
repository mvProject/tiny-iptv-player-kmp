package com.mvproject.tinyiptvkmp.features.groups.state

sealed interface GroupUiState {
    data object Loading : GroupUiState

    data object Groups : GroupUiState

    data object Empty : GroupUiState
}
