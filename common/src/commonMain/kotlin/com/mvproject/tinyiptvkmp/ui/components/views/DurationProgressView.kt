/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun DurationProgressView(
    modifier: Modifier = Modifier,
    progress: Float,
    trackColor: Color = MaterialTheme.colorScheme.primary,
    backColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.size2),
        trackColor = trackColor,
        color = backColor,
        strokeCap = StrokeCap.Round
    )
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewDurationProgressView() {
    VideoAppTheme(darkTheme = true) {
        DurationProgressView(progress = 0.5f)
    }
}*/
