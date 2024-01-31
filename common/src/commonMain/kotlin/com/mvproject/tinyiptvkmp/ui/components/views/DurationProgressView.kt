/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 31.01.24, 09:26
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
    durationColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.size2),
        trackColor = trackColor,
        color = durationColor,
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
