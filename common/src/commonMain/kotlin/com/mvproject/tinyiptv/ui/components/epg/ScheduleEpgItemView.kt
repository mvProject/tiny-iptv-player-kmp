/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:44
 *
 */

package com.mvproject.tinyiptv.ui.components.epg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.mvproject.tinyiptv.data.model.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.views.DurationProgressView
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.tinyiptv.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.tinyiptv.utils.TimeUtils.convertTimeToReadableFormat

@Composable
fun ScheduleEpgItemView(
    modifier: Modifier = Modifier,
    program: EpgProgram,
) {

    val isProgramEnded by remember {
        derivedStateOf {
            program.programProgress > PROGRESS_STATE_COMPLETE
        }
    }

    val isProgramProgressShow by remember {
        derivedStateOf {
            program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE
        }
    }

    val cardAlpha =
        if (isProgramEnded)
            MaterialTheme.dimens.alpha50
        else MaterialTheme.dimens.alphaDefault

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
    ) {
        if (isProgramProgressShow) {
            DurationProgressView(progress = program.programProgress)
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))
        }

        val text = StringBuilder().apply {
            append(program.start.convertTimeToReadableFormat())
            append(" - ")
            append(program.title)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewScheduleEpgItemView() {
    VideoAppTheme(darkTheme = true) {
        ScheduleEpgItemView(program = PreviewTestData.testEpgProgram)
    }
}*/
