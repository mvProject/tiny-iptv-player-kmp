/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.features.playlist.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mvproject.tinyiptvkmp.core.components.adaptive.adaptiveContentWidth
import com.mvproject.tinyiptvkmp.core.components.adaptive.rememberAdaptiveLayoutState
import com.mvproject.tinyiptvkmp.core.components.buttons.ActionButton
import com.mvproject.tinyiptvkmp.core.components.buttons.SelectButton
import com.mvproject.tinyiptvkmp.core.components.indicators.LoadingIndicator
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.components.selectors.OptionSelector
import com.mvproject.tinyiptvkmp.core.components.selectors.optionSetOf
import com.mvproject.tinyiptvkmp.core.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.hint_update_period
import com.mvproject.tinyiptvkmp.core.foundation.common.WEIGHT_1
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.typeM3U
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.typeM3U8
import com.mvproject.tinyiptvkmp.core.mapper.mapToString
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.Res
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.btn_add_local
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.btn_save
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.btn_update
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.hint_address
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.hint_name
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.label_or
import com.mvproject.tinyiptvkmp.features.playlist.presentation.generated.resources.msg_playlist_details
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.stringResource
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun PlaylistScreen(viewModel: PlaylistViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    PlaylistScreen(
        state = uiState,
        onAction = viewModel::onIntent,
    )
}

@Composable
private fun PlaylistScreen(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit = {},
) {
    val adaptiveLayoutState = rememberAdaptiveLayoutState()
    val periods = optionSetOf(
        values = UpdatePeriod.entries,
        label = { period -> stringResource(period.mapToString()) },
    )

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onAction(PlaylistAction.NavigateBack)
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
                onAction(PlaylistAction.ImportLocalFile(file = file))
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
                onBackClick = { onAction(PlaylistAction.NavigateBack) },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .adaptiveContentWidth(adaptiveLayoutState)
                        .padding(
                            horizontal = adaptiveLayoutState.contentHorizontalPadding,
                            vertical = MaterialTheme.dimensionSize.size12,
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PlaylistTextField(
                    label = stringResource(Res.string.hint_name),
                    value = state.playlistName,
                    onValueChange = { title -> onAction(PlaylistAction.SetTitle(title = title)) },
                )

                SpacerHeight(height = MaterialTheme.dimensionSize.size8)

                PlaylistTextField(
                    label = stringResource(Res.string.hint_address),
                    value = if (state.playlistType == PlaylistType.LOCAL) {
                        state.playlistName
                    } else {
                        state.playlistSource
                    },
                    enabled = state.playlistType == PlaylistType.REMOTE,
                    onValueChange = { url -> onAction(PlaylistAction.SetRemoteUrl(url = url)) },
                )

                if (!state.isEdit) {

                    SpacerHeight(height = MaterialTheme.dimensionSize.size16)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                space = MaterialTheme.dimensionSize.size8,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(WEIGHT_1),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )

                        Text(
                            text = stringResource(Res.string.label_or),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(WEIGHT_1),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }

                    SpacerHeight(height = MaterialTheme.dimensionSize.size16)

                    SelectButton(
                        title = stringResource(Res.string.btn_add_local),
                        onClick = {
                            launcher.launch()
                        },
                    )
                }

                SpacerHeight(height = MaterialTheme.dimensionSize.size16)

                if (state.playlistType == PlaylistType.REMOTE) {
                    OptionSelector(
                        label = stringResource(DesignSystemRes.string.hint_update_period),
                        options = periods.values,
                        optionLabel = periods.label,
                        selected = UpdatePeriod.entries.getOrNull(state.updatePeriod),
                        onSelected = { period ->
                            onAction(PlaylistAction.SetUpdatePeriod(period = period.ordinal))
                        },
                    )
                }

                SpacerHeight(weight = MaterialTheme.dimensionWeight.weight1)

                ActionButton(
                    title =
                        stringResource(
                            if (state.isEdit) {
                                Res.string.btn_update
                            } else {
                                Res.string.btn_save
                            },
                        ),
                    enabled = state.isReadyToSave,
                    onClick = {
                        if (state.isEdit) {
                            onAction(PlaylistAction.UpdatePlaylist)
                        } else {
                            onAction(PlaylistAction.SavePlaylist)
                        }
                    },
                    modifier =
                        Modifier
                            .padding(vertical = MaterialTheme.dimensionSize.size8)
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.ime),
                )
            }

            LoadingIndicator(
                isVisible = state.isSaving || state.isImportingLocalFile,
            )
        }
    }
}

/**
 * Shared field styling for the playlist form. The disabled state keeps a real container tone so a
 * read-only field still reads as read-only, instead of a hardcoded grey.
 */
@Composable
private fun PlaylistTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorSchemeExtended.activeInput,
            )
        },
        textStyle = MaterialTheme.typography.labelMedium,
        colors =
            TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                cursorColor = MaterialTheme.colorSchemeExtended.activeInput,
                focusedIndicatorColor = MaterialTheme.colorSchemeExtended.activeInput,
            ),
    )
}
