/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:53
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.data.PreviewTestData
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.ui.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.ui.components.modifiers.SpacerHeight
import com.mvproject.tinyiptvkmp.ui.components.modifiers.SpacerWidth
import com.mvproject.tinyiptvkmp.ui.components.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.ui.components.views.TimeItem
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tinyiptvkmp.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tinyiptvkmp.utils.CommonUtils.delimiterDash
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ChannelProgramItem(
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
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        ProgramTitle(
            modifier = Modifier
                .padding(top = MaterialTheme.dimens.size8)
                .padding(horizontal = MaterialTheme.dimens.size8),
            title = program.title,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.size8)
                .padding(bottom = MaterialTheme.dimens.size8),
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

            SpacerWidth(weight = MaterialTheme.dimens.weight1)

            ProgramDuration(
                start = program.dateTimeStart,
                end = program.dateTimeEnd,
                timeColor = contentColor,
                timeStyle = MaterialTheme.typography.labelMedium
            )
        }

        if (isProgramProgressShow) {
            SpacerHeight(MaterialTheme.dimens.size8)
            ProgramProgressIndicator(progress = program.programProgress)
        }
    }
}

@Preview
@Composable
private fun ProgramItemPreview() {
    VideoAppTheme {
        ChannelProgramItem(program = PreviewTestData.testEpgProgram)
    }
}
