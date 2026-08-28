/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.features.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.components.TimeItem
import com.mvproject.tinyiptvkmp.core.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.core.components.modifiers.roundedHeader
import com.mvproject.tinyiptvkmp.core.components.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.msg_no_epg_found
import com.mvproject.tinyiptvkmp.core.foundation.common.COUNT_ZERO_FLOAT
import com.mvproject.tinyiptvkmp.core.foundation.common.PROGRESS_STATE_COMPLETE
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.delimiterDash
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.space
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@Composable
fun ChannelPrograms(
    modifier: Modifier = Modifier,
    title: String = String.empty,
    programs: List<EpgProgram>,
) {
    Column(
        modifier = modifier
        //  .clip(MaterialTheme.shapes.small)
    ) {
        if (title.isNotBlank()) {
            ChannelProgramsTitle(
                modifier = Modifier.roundedHeader(),
                title = title
            )
        }

        if (programs.isEmpty()) {
            ChannelProgramsEmpty(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(Res.string.msg_no_epg_found)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size2)
        ) {
            items(
                items = programs,
                key = { program -> program.programId },
            ) { program ->
                ChannelProgramItem(program = program)
            }
        }
    }
}

@Composable
private fun ChannelProgramItem(
    modifier: Modifier = Modifier,
    program: EpgProgram,
) {
    val isProgramProgressShow by remember {
        derivedStateOf {
            program.programProgress > COUNT_ZERO_FLOAT &&
                    program.programProgress <= PROGRESS_STATE_COMPLETE
        }
    }

    val contentColor =
        if (isProgramProgressShow) {
            MaterialTheme.colorSchemeExtended.activeProgramTitle
        } else {
            MaterialTheme.colorSchemeExtended.programTitle
        }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        ProgramTitle(
            modifier = Modifier
                .padding(top = MaterialTheme.dimensionSize.size8)
                .padding(horizontal = MaterialTheme.dimensionSize.size8),
            title = program.title,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimensionSize.size8)
                .padding(bottom = MaterialTheme.dimensionSize.size8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val (hourStart, minuteStart) = program.dateTimeStart.convertToTime()
            TimeItem(
                hour = hourStart,
                minute = minuteStart,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium
            )

            Text(
                text = String.delimiterDash,
                color = contentColor,
            )
            val (hourEnd, minuteEnd) = program.dateTimeEnd.convertToTime()
            TimeItem(
                hour = hourEnd,
                minute = minuteEnd,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium
            )

            SpacerWidth(weight = MaterialTheme.dimensionWeight.weight1)

            ProgramDuration(
                start = program.dateTimeStart,
                end = program.dateTimeEnd,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium
            )
        }

        if (isProgramProgressShow) {
            SpacerHeight(MaterialTheme.dimensionSize.size8)
            ProgramProgressIndicator(
                progress = program.programProgress
            )
        }
    }
}

@Composable
private fun ChannelProgramsTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = style,
        color = color,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ChannelProgramsEmpty(
    modifier: Modifier = Modifier,
    title: String = String.empty
) {
    Box(
        modifier = modifier,
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

@Composable
private fun ProgramDuration(
    modifier: Modifier = Modifier,
    start: Long,
    end: Long,
    timeColor: Color = MaterialTheme.colorScheme.onSurface,
    timeStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val (hours, minutes) = calculateDuration(start = start, end = end)

    val durationText = buildString {
        if (hours > 0) {
            append(hours)
            append(String.space)
            append("hr")
            append(String.space)
        }
        if (minutes > 0) {
            append(minutes)
            append(String.space)
            append("min")
        }
    }
    Text(
        modifier = modifier,
        text = durationText,
        textAlign = TextAlign.Center,
        style = timeStyle,
        color = timeColor,
    )
}

private fun calculateDuration(
    start: Long,
    end: Long
): Pair<Long, Long> {
    val duration = (end - start).milliseconds
    val hours = duration.inWholeHours
    val minutes = duration.inWholeMinutes % 60

    return Pair(hours, minutes)
}

private fun Long.convertToTime(): Pair<String, String> {
    val local = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val hour = local.hour.toString().padStart(2, '0')
    val minute = local.minute.toString().padStart(2, '0')
    return Pair(hour, minute)
}

@Composable
@Preview
private fun ChannelProgramsPreview() {
    AppTheme {
        ChannelPrograms(
            programs = listOf(
                EpgProgram(
                    programId = "preview-program",
                    title = "Morning news",
                    channelId = "preview-channel",
                    dateTimeStart = 0L,
                    dateTimeEnd = 3_600_000L,
                    description = "Preview program",
                )
            )
        )
    }
}
