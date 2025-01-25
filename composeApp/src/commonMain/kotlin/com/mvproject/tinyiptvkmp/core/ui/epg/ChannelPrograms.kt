/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.core.ui.epg

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
import com.mvproject.tinyiptvkmp.core.common.COUNT_ZERO_FLOAT
import com.mvproject.tinyiptvkmp.core.common.PROGRESS_STATE_COMPLETE
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.delimiterDash
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.space
import com.mvproject.tinyiptvkmp.core.common.utils.calculateDuration
import com.mvproject.tinyiptvkmp.core.domain.PreviewTestData
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import com.mvproject.tinyiptvkmp.core.ui.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.ui.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.core.ui.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.core.ui.modifiers.roundedHeader
import com.mvproject.tinyiptvkmp.core.ui.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.core.ui.views.TimeItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@Composable
internal fun ChannelPrograms(
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
            TimeItem(
                timeStamp = program.dateTimeStart,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium
            )

            Text(
                text = String.delimiterDash,
                color = contentColor,
            )

            TimeItem(
                timeStamp = program.dateTimeEnd,
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
            ProgramProgressIndicator(progress = program.programProgress)
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

@Composable
@Preview
private fun ChannelProgramsPreview() {
    AppTheme {
        ChannelPrograms(programs = PreviewTestData.testEpgPrograms)
    }
}
