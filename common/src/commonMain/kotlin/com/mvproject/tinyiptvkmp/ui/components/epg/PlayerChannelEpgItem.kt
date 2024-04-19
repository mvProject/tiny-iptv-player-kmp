/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:23
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.ui.components.views.DurationProgressView
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tinyiptvkmp.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tinyiptvkmp.utils.TimeUtils.convertTimeToReadableFormat

@Composable
fun PlayerChannelEpgItem(epgProgram: EpgProgram) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = epgProgram.title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))

        val isProgramProgressShow by remember {
            derivedStateOf {
                epgProgram.programProgress > COUNT_ZERO_FLOAT && epgProgram.programProgress <= PROGRESS_STATE_COMPLETE
            }
        }

        if (isProgramProgressShow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        MaterialTheme.dimens.size4,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = epgProgram.start.convertTimeToReadableFormat(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                DurationProgressView(
                    modifier = Modifier.weight(6f),
                    progress = epgProgram.programProgress,
                )

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = epgProgram.stop.convertTimeToReadableFormat(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerEpgItemView() {
    VideoAppTheme(darkTheme = true) {
        PlayerChannelEpgItem(epgProgram = PreviewTestData.testEpgProgram)
    }
}*/
