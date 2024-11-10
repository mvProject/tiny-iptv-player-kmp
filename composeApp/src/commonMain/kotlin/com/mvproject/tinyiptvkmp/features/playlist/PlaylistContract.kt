package com.mvproject.tinyiptvkmp.features.playlist

import androidx.compose.runtime.Stable
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.domain.model.Playlist

interface PlaylistContract {
    @Stable
    data class UiState(
        val selectedId: String = String.empty,
        val playlistName: String = String.empty,
        val playlistSource: String = String.empty,
        val playlistType: PlaylistType = PlaylistType.REMOTE,
        val updatePeriod: Int = UpdatePeriod.NO_UPDATE.value,
        val lastUpdateDate: Long = LONG_VALUE_ZERO,
        val isSaving: Boolean = false,
        val isEdit: Boolean = false,
        val isComplete: Boolean = false,
    ) {
        val isReadyToSave: Boolean
            get() = playlistName.isNotBlank() && playlistSource.isNotBlank()

        fun toPlaylist() =
            with(this) {
                Playlist(
                    id = selectedId,
                    playlistName = playlistName,
                    playlistSource = playlistSource,
                    playlistType = playlistType,
                    lastUpdateDate = lastUpdateDate,
                    updatePeriod = updatePeriod.toLong(),
                )
            }
    }

    sealed interface UiAction {
        data class SetTitle(val title: String) : UiAction
        data class SetRemoteUrl(val url: String) : UiAction
        data class SetLocalUri(val name: String, val uri: String) : UiAction
        data class SetUpdatePeriod(val period: Int) : UiAction
        data object SavePlaylist : UiAction
        data object UpdatePlaylist : UiAction
        data object NavigateBack : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
    }
}