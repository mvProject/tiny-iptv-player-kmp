/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.04.24, 17:46
 *
 */

package com.mvproject.tinyiptvkmp.core.ui.views

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.delimiterTime
import com.mvproject.tinyiptvkmp.core.common.utils.convertToTime
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    timeStamp: Long,
    timeColor: Color = MaterialTheme.colorScheme.onSurface,
    timeStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val (hour, minute) = timeStamp.convertToTime()

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(MaterialTheme.dimensionSize.size22),
            text = hour,
            textAlign = TextAlign.Center,
            style = timeStyle,
            color = timeColor,
        )
        Text(
            text = String.delimiterTime,
            style = timeStyle,
            color = timeColor,
        )
        Text(
            modifier = Modifier.width(MaterialTheme.dimensionSize.size22),
            text = minute,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = timeColor,
        )
    }
}
