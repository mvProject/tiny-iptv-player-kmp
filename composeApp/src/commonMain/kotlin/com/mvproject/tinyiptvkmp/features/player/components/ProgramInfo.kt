/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:50
 *
 */

package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.theme.dimensionFraction
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.ui.modifiers.roundedHeader
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@Composable
fun ProgramInfo(
    channelName: String = String.empty,
    programName: String = String.empty,
    description: String = String.empty,
) {
    Column(
        modifier =
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(MaterialTheme.dimensionFraction.fraction80)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small,
            ),
    ) {
        ProgramTitle(
            modifier = Modifier.roundedHeader(),
            title = programName.ifEmpty { channelName }
        )

        if (description.isEmpty()) {
            ProgramDescriptionEmpty(
                modifier = Modifier.height(MaterialTheme.dimensionSize.size180),
                title = stringResource(Res.string.msg_no_epg_found)
            )
        } else {
            ProgramDescription(
                modifier = Modifier.padding(all = MaterialTheme.dimensionSize.size16),
                title = description
            )
        }
    }
}

@Composable
private fun ProgramTitle(
    modifier: Modifier = Modifier,
    title: String = String.empty
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ProgramDescription(
    modifier: Modifier = Modifier,
    title: String = String.empty
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun ProgramDescriptionEmpty(
    modifier: Modifier = Modifier,
    title: String = String.empty
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimensionSize.size12),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewOverlayChannelInfo() {
    VideoAppTheme(darkTheme = true) {
        OverlayChannelInfo(
            currentChannel = TvPlaylistChannel(
                channelName = "Test",
                channelEpg = listOf(
                    EpgProgram(
                        title = "Epg",
                        channelId = "id",
                        start = System.currentTimeMillis() - 30.minutes.inWholeMilliseconds,
                        stop = System.currentTimeMillis() + 30.minutes.inWholeMilliseconds,
                        description = "Epg Description"
                    )
                )
            )
        )
    }
}*/
