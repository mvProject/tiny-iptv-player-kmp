/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:46
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.PROGRAM_TIME_MEASURE_COUNT
import com.mvproject.tinyiptvkmp.utils.AppConstants.PROGRAM_TIME_MEASURE_DELIMITER

@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    time: String,
    timeColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val datTime =
        time
            .split(PROGRAM_TIME_MEASURE_DELIMITER)
            .take(PROGRAM_TIME_MEASURE_COUNT)

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(MaterialTheme.dimens.size22),
            text = datTime.first(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = timeColor,
        )
        Text(
            text = PROGRAM_TIME_MEASURE_DELIMITER,
            style = MaterialTheme.typography.bodySmall,
            color = timeColor,
        )
        Text(
            modifier = Modifier.width(MaterialTheme.dimens.size22),
            text = datTime.last(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = timeColor,
        )
    }
}
