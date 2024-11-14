/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.tinyiptvkmp.core.common.AppConstants.WEIGHT_1
import com.mvproject.tinyiptvkmp.core.common.mvi.CollectUiEffect
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.tmpFolder
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.typeM3U
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.typeM3U8
import com.mvproject.tinyiptvkmp.core.domain.enums.PlaylistType
import com.mvproject.tinyiptvkmp.core.domain.enums.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.ui.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.ui.overlay.OverlayContent
import com.mvproject.tinyiptvkmp.core.ui.overlay.OverlayOptionsMenu
import com.mvproject.tinyiptvkmp.core.ui.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.core.ui.toolbars.AppBarWithBackNav
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import okio.buffer
import okio.sink
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.btn_add_local
import tinyiptvkmp.composeapp.generated.resources.btn_save
import tinyiptvkmp.composeapp.generated.resources.btn_update
import tinyiptvkmp.composeapp.generated.resources.hint_address
import tinyiptvkmp.composeapp.generated.resources.hint_name
import tinyiptvkmp.composeapp.generated.resources.hint_update_period
import tinyiptvkmp.composeapp.generated.resources.label_or
import tinyiptvkmp.composeapp.generated.resources.msg_playlist_details

@Composable
internal fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            PlaylistUiEffect.OnNavigateBack -> onNavigateBack()
        }
    }
    PlaylistScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun PlaylistScreen(
    uiState: PlaylistUiState,
    onAction: (PlaylistUiAction) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            onAction(PlaylistUiAction.NavigateBack)
        }
    }

    val launcher =
        rememberFilePickerLauncher(
            mode = PickerMode.Single,
            type =
            PickerType.File(
                extensions =
                listOf(
                    String.typeM3U,
                    String.typeM3U8,
                ),
            ),
            title = stringResource(Res.string.btn_add_local),
        ) { selectedFile ->
            selectedFile?.let { file ->
                val folderFileTmp = tmpFolder / file.name
                val fileTmp = folderFileTmp.toFile()

                scope.launch {
                    //FileOutputStream(fileTmp).use {
                    //    it.write(file.readBytes())
                    //}
                    fileTmp.sink().buffer().use { sink ->
                        sink.write(file.readBytes())
                    }
                }

                onAction(
                    PlaylistUiAction.SetLocalUri(
                        name = file.name,
                        uri = folderFileTmp.toString(),
                    )
                )
            }
        }

    Scaffold(
        modifier =
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(Res.string.msg_playlist_details),
                onBackClick = { onAction(PlaylistUiAction.NavigateBack) },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            val isUpdateOptionOpen = remember { mutableStateOf(false) }

            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size12),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.playlistName,
                    onValueChange = {
                        onAction(PlaylistUiAction.SetTitle(title = it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.hint_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )

                SpacerHeight(height = MaterialTheme.dimens.size8)

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.playlistType == PlaylistType.REMOTE,
                    value = if (uiState.playlistType == PlaylistType.LOCAL) uiState.playlistName else uiState.playlistSource,
                    onValueChange = {
                        onAction(PlaylistUiAction.SetRemoteUrl(url = it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.hint_address),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.DarkGray,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )

                SpacerHeight(height = MaterialTheme.dimens.size8)
                OptionSelector(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.hint_update_period),
                    enabled = uiState.playlistType == PlaylistType.REMOTE,
                    selectedItem = stringResource(UpdatePeriod.entries[uiState.updatePeriod].title),
                    isExpanded = isUpdateOptionOpen.value,
                    onClick = {
                        isUpdateOptionOpen.value = true
                    },
                )

                SpacerHeight(height = MaterialTheme.dimens.size16)
                if (!uiState.isEdit) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                        Arrangement.spacedBy(
                            space = MaterialTheme.dimens.size8,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(WEIGHT_1),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )

                        Text(
                            text = stringResource(Res.string.label_or),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(WEIGHT_1),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }

                    SpacerHeight(height = MaterialTheme.dimens.size16)
                    OutlinedButton(
                        onClick = {
                            launcher.launch()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(
                            text = stringResource(Res.string.btn_add_local),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }

                SpacerHeight(weight = MaterialTheme.dimens.weight1)
                ElevatedButton(
                    enabled = uiState.isReadyToSave,
                    onClick = {
                        if (uiState.isEdit) {
                            onAction(PlaylistUiAction.UpdatePlaylist)
                        } else {
                            onAction(PlaylistUiAction.SavePlaylist)
                        }
                    },
                    modifier =
                    Modifier
                        .padding(vertical = MaterialTheme.dimens.size8)
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.ime),
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    shape = MaterialTheme.shapes.small,
                ) {
                    val text =
                        if (uiState.isEdit) {
                            Res.string.btn_update
                        } else {
                            Res.string.btn_save
                        }

                    Text(
                        text = stringResource(text),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            LoadingIndicator(
                isVisible = uiState.isSaving,
            )

            OverlayContent(
                isVisible = isUpdateOptionOpen.value,
                onViewTap = { isUpdateOptionOpen.value = false },
            ) {
                OverlayOptionsMenu(
                    title = stringResource(Res.string.hint_update_period),
                    selectedIndex = uiState.updatePeriod,
                    options = UpdatePeriod.entries.map { stringResource(it.title) },
                    onItemSelected = { index ->
                        onAction(PlaylistUiAction.SetUpdatePeriod(period = index))
                        isUpdateOptionOpen.value = false
                    },
                )
            }
        }
    }
}

// todo replace preview
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistDetailViewContent() {
    VideoAppTheme(darkTheme = true) {
        PlaylistView(
            state = PlaylistState()
        )
    }
}
*/
