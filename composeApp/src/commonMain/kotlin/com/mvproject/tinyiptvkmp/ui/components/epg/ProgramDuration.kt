/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:46
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.utils.CommonUtils.space
import com.mvproject.tinyiptvkmp.utils.TimeUtils.calculateDuration

@Composable
internal fun ProgramDuration(
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
