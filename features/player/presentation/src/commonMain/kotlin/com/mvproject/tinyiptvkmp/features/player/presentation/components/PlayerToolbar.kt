/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 18:14
 *
 */

package com.mvproject.tinyiptvkmp.features.player.presentation.components

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
import com.mvproject.tinyiptvkmp.core.components.TimeItem
import com.mvproject.tinyiptvkmp.core.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.components.modifiers.roundedHeader
import com.mvproject.tinyiptvkmp.core.foundation.model.VideoSize
import com.mvproject.tinyiptvkmp.core.foundation.utils.convertToTime
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionOpacity
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerAction

@Composable
fun PlayerToolbar(
    modifier: Modifier = Modifier,
    currentChannel: TvChannelWithPrograms,
    programCount: Int = 2,
    videoSize: VideoSize,
    isVisible: Boolean = false,
    isPlaying: Boolean = false,
    isFullScreen: Boolean = false,
    onAction: (PlayerAction) -> Unit = {},
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        Box(modifier = modifier) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .alpha(MaterialTheme.dimensionOpacity.opacity80)
                    .roundedHeader(color = MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size8),
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
                    videoSize = videoSize,
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
            MaterialTheme.colorSchemeExtended.activeProgramTitle
        } else {
            MaterialTheme.colorSchemeExtended.programTitle
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
    programProgress: Float,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size8)
    ) {
        val (hourStart, minuteStart) = programStart.convertToTime()
        TimeItem(
            hour = hourStart,
            minute = minuteStart,
            timeColor = MaterialTheme.colorSchemeExtended.timeColor
        )

        ProgramProgressIndicator(
            modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight1),
            progress = programProgress
        )
        val (hourEnd, minuteEnd) = programEnd.convertToTime()
        TimeItem(
            hour = hourEnd,
            minute = minuteEnd,
            timeColor = MaterialTheme.colorSchemeExtended.timeColor
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
