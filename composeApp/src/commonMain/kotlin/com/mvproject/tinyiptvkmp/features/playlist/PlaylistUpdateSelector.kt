package com.mvproject.tinyiptvkmp.features.playlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.hint_update_period
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod
import com.mvproject.tinyiptvkmp.core.mapper.mapToString
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionText
import com.mvproject.tinyiptvkmp.features.playlist.api.domain.model.PlaylistType
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaylistUpdateSelector(
    uiState: PlaylistUiState,
    onAction: (PlaylistUiAction) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth().border(
            width = MaterialTheme.dimensionSize.size1,
            color = MaterialTheme.colorScheme.onSurface,
            shape = MaterialTheme.shapes.extraSmall
        )
    ) {
        if (uiState.playlistType == PlaylistType.REMOTE) {
            val options = UpdatePeriod.entries.map { stringResource(it.mapToString()) }

            var isSelectPlaylistOpen by remember { mutableStateOf(false) }

            ListItem(
                modifier = Modifier.clickable {
                    isSelectPlaylistOpen = !isSelectPlaylistOpen
                },
                overlineContent = {
                    Text(
                        text = stringResource(Res.string.hint_update_period),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = MaterialTheme.dimensionText.font10,
                    )
                },
                headlineContent = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = options[uiState.updatePeriod],
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                trailingContent = {
                    val trailingIcon = if (isSelectPlaylistOpen)
                        Icons.Default.ArrowDropUp
                    else
                        Icons.Default.ArrowDropDown

                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = trailingIcon.name
                    )

                }
            )

            AnimatedContent(
                targetState = isSelectPlaylistOpen,
                transitionSpec = ({
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) togetherWith slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up
                    )
                })
            ) { isOpen ->

                if (isOpen) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(options) { index, item ->
                            val isSelected = index == uiState.updatePeriod

                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(),
                                onClick = {
                                    isSelectPlaylistOpen = false
                                    onAction(PlaylistUiAction.SetUpdatePeriod(period = index))
                                },
                            ) {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.titleSmall,
                                    color =
                                    if (isSelected) {
                                        MaterialTheme.colorSchemeExtended.activeInput
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                )
                            }

                            if (index < options.lastIndex) {
                                HorizontalDivider(
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimensionSize.size16),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
