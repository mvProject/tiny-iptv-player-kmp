package com.mvproject.tinyiptvkmp.features.playlist.presentation

import androidx.compose.runtime.Immutable
import com.mvproject.tinyiptvkmp.core.foundation.common.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.Playlist
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import io.github.vinceglb.filekit.core.PlatformFile

@Immutable
data class PlaylistState(
    val selectedId: String = String.empty,
    val playlistName: String = String.empty,
    val playlistSource: String = String.empty,
    val playlistType: PlaylistType = PlaylistType.REMOTE,
    val updatePeriod: Int = UpdatePeriod.NO_UPDATE.value,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val isSaving: Boolean = false,
    val isImportingLocalFile: Boolean = false,
    val isEdit: Boolean = false,
    val isComplete: Boolean = false,
) {
    val isReadyToSave: Boolean
        get() = playlistName.isNotBlank() &&
                playlistSource.isNotBlank() &&
                !isSaving &&
                !isImportingLocalFile
}

internal fun PlaylistState.toPlaylist() = with(this) {
    Playlist(
        id = selectedId,
        playlistName = playlistName,
        playlistSource = playlistSource,
        playlistType = playlistType,
        lastUpdateDate = lastUpdateDate,
        updatePeriod = updatePeriod.toLong(),
    )
}

sealed interface PlaylistAction {
    data class SetTitle(val title: String) : PlaylistAction
    data class SetRemoteUrl(val url: String) : PlaylistAction
    data class SetLocalUri(val name: String, val uri: String) : PlaylistAction
    data class ImportLocalFile(val file: PlatformFile) : PlaylistAction
    data class SetUpdatePeriod(val period: Int) : PlaylistAction
    data class SavePlaylistFailed(val throwable: Throwable) : PlaylistAction
    data object SavePlaylist : PlaylistAction
    data object SavePlaylistCompleted : PlaylistAction
    data object UpdatePlaylist : PlaylistAction
    data object NavigateBack : PlaylistAction
}

sealed interface PlaylistEffect {
    data class CreatePlaylist(val playlist: Playlist) : PlaylistEffect
    data class UpdatePlaylist(val playlist: Playlist) : PlaylistEffect
}


data class PlaylistDetailArgs(
    val playlistId: String,
)
