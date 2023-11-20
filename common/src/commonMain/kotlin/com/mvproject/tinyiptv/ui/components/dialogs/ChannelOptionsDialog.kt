/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptv.ui.components.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun ChannelOptionsDialog(
    isDialogOpen: MutableState<Boolean>,
    isEpgEnabled: Boolean = false,
    isEpgUsing: Boolean = false,
    isInFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onShowEpg: () -> Unit = {},
    onToggleEpgState: () -> Unit = {}
) {
    if (isDialogOpen.value) {
        Dialog(
            onDismissRequest = { isDialogOpen.value = false }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(MaterialTheme.dimens.size8),
                shape = MaterialTheme.shapes.medium,
                shadowElevation = MaterialTheme.dimens.size8
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.size24),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        enabled = isEpgUsing,
                        border = BorderStroke(
                            width = MaterialTheme.dimens.size1,
                            color = MaterialTheme.colorScheme.outline
                        ),
                        contentPadding = PaddingValues(),
                        onClick = onToggleEpgState
                    ) {
                        Text(
                            text = if (isEpgEnabled) "Disable Epg" else "Enable Epg",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEpgEnabled,
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(
                            width = MaterialTheme.dimens.size1,
                            color = MaterialTheme.colorScheme.outline
                        ),
                        contentPadding = PaddingValues(),
                        onClick = onShowEpg
                    ) {
                        Text(
                            text = "Show Epg",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(
                            width = MaterialTheme.dimens.size1,
                            color = MaterialTheme.colorScheme.outline
                        ),
                        contentPadding = PaddingValues(),
                        onClick = onToggleFavorite
                    ) {
                        Text(
                            text = if (isInFavorite) "Remove from Fav" else "Add to Fav",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
        }
    }
}

// todo replace preview
/*
@Preview(showBackground = true)
@Composable
fun PreviewChannelOptionsDialog() {
    VideoAppTheme(darkTheme = true) {
        ChannelOptionsDialog(
            isDialogOpen = mutableStateOf(true)
        )
    }
}*/
