package com.mvproject.tinyiptvkmp.core.components.channels

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
import com.mvproject.tinyiptvkmp.core.foundation.utils.calculateDuration
import com.mvproject.tinyiptvkmp.core.foundation.utils.convertToTime
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChannelPrograms(
    programs: List<ChannelProgramUiModel>,
    modifier: Modifier = Modifier,
    title: String = String.empty,
) {
    Column(modifier = modifier) {
        if (title.isNotBlank()) {
            ChannelProgramsTitle(
                modifier = Modifier.roundedHeader(),
                title = title,
            )
        }

        if (programs.isEmpty()) {
            ChannelProgramsEmpty(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(Res.string.msg_no_epg_found),
            )
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size2),
        ) {
            items(
                items = programs,
                key = { program -> program.id },
            ) { program ->
                ChannelProgramItem(program = program)
            }
        }
    }
}

@Composable
private fun ChannelProgramItem(
    program: ChannelProgramUiModel,
    modifier: Modifier = Modifier,
) {
    val isProgramProgressShow =
        program.progress > COUNT_ZERO_FLOAT &&
                program.progress <= PROGRESS_STATE_COMPLETE

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
            style = MaterialTheme.typography.bodyMedium,
        )

        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimensionSize.size8)
                .padding(bottom = MaterialTheme.dimensionSize.size8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val (hourStart, minuteStart) = program.startMillis.convertToTime()
            TimeItem(
                hour = hourStart,
                minute = minuteStart,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium,
            )

            Text(
                text = String.delimiterDash,
                color = contentColor,
            )
            val (hourEnd, minuteEnd) = program.endMillis.convertToTime()
            TimeItem(
                hour = hourEnd,
                minute = minuteEnd,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium,
            )

            SpacerWidth(weight = MaterialTheme.dimensionWeight.weight1)

            ProgramDuration(
                start = program.startMillis,
                end = program.endMillis,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium,
            )
        }

        if (isProgramProgressShow) {
            SpacerHeight(MaterialTheme.dimensionSize.size8)
            ProgramProgressIndicator(progress = program.progress)
        }
    }
}

@Composable
private fun ChannelProgramsTitle(
    title: String,
    modifier: Modifier = Modifier,
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
    title: String = String.empty,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size12),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ProgramDuration(
    start: Long,
    end: Long,
    modifier: Modifier = Modifier,
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
