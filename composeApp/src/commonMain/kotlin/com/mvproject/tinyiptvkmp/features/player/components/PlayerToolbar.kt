/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:14
 *
 */

package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.theme.dimens
import com.mvproject.tinyiptvkmp.core.ui.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.ui.modifiers.roundedHeader
import com.mvproject.tinyiptvkmp.core.ui.views.TimeItem
import com.mvproject.tinyiptvkmp.features.player.PlayerContract.UiAction

@Composable
fun PlayerToolbar(
    modifier: Modifier = Modifier,
    currentChannel: TvChannel,
    programCount: Int = 2,
    isVisible: Boolean = false,
    isPlaying: Boolean = false,
    isFullScreen: Boolean = false,
    onAction: (UiAction) -> Unit = {},
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        Box(
            modifier =
            modifier
                .alpha(MaterialTheme.dimens.alpha80),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .roundedHeader(color = MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
            ) {

                PlayerChannel(
                    modifier = Modifier.fillMaxWidth(),
                    title = currentChannel.channelName,
                )

                if (currentChannel.programs.isNotEmpty()) {

                    PlayerPrograms(programs = currentChannel.programs.take(programCount))

                    PlayerProgress(
                        programStart = currentChannel.programs.first().dateTimeStart,
                        programEnd = currentChannel.programs.first().dateTimeEnd,
                        programProgress = currentChannel.programs.first().programProgress
                    )
                }

                PlayerControls(
                    modifier = Modifier.fillMaxWidth(),
                    isFavorite = currentChannel.favoriteType != FavoriteType.NONE,
                    isPlaying = isPlaying,
                    isFullScreen = isFullScreen,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun PlayerChannel(
    modifier: Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun PlayerPrograms(programs: List<EpgProgram>) {
    programs.forEachIndexed { index, program ->
        val color = if (index == 0) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }

        Text(
            text = program.title,
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}

@Composable
private fun PlayerProgress(
    modifier: Modifier = Modifier,
    programStart: Long,
    programEnd: Long,
    programProgress: Float
,) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8)
    ) {
        TimeItem(
            timeStamp = programStart,
            timeColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ProgramProgressIndicator(
            modifier = Modifier.weight(MaterialTheme.dimens.weight1),
            progress = programProgress
        )

        TimeItem(
            timeStamp = programEnd,
            timeColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerChannelView() {
    VideoAppTheme(darkTheme = true) {
        PlayerChannelView(
            currentChannel = TvPlaylistChannel(
                channelName = "Test",
                channelEpg = listOf(
                    EpgProgram(
                        title = "Epg Title",
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
