/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:53
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.ui.components.views.DurationProgressView
import com.mvproject.tinyiptvkmp.ui.components.views.TimeItem
import com.mvproject.tinyiptvkmp.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tinyiptvkmp.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tinyiptvkmp.utils.TimeUtils.convertTimeToReadableFormat

@Composable
fun PlayerEpgItem(
    modifier: Modifier = Modifier,
    program: EpgProgram,
) {
    val isProgramProgressShow by remember {
        derivedStateOf {
            program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE
        }
    }

    val contentColor =
        if (isProgramProgressShow) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    Column {
        ListItem(
            colors =
                ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                ),
            leadingContent = {
                TimeItem(
                    time = program.start.convertTimeToReadableFormat(),
                    timeColor = contentColor,
                )
            },
            headlineContent = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = program.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                )
            },
        )

        if (isProgramProgressShow) {
            DurationProgressView(progress = program.programProgress)
        }
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerOverlayEpgItemView() {
    VideoAppTheme(darkTheme = true) {
        PlayerEpgItem(program = PreviewTestData.testEpgProgram)
    }
}*/
