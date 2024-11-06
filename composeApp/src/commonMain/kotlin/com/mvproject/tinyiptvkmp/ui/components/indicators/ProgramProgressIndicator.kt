/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 23.02.24, 11:07
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.indicators

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProgramProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    trackColor: Color = MaterialTheme.colorScheme.primary,
    durationColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier.fillMaxWidth().height(MaterialTheme.dimens.size1),
        color = durationColor,
        trackColor = trackColor,
        strokeCap = StrokeCap.Butt,
        drawStopIndicator = {}
    )
}

@Preview
@Composable
private fun ProgramProgressIndicatorPreview() {
    VideoAppTheme {
        ProgramProgressIndicator(progress = 0.5f)
    }
}
