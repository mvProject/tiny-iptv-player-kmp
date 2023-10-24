/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 19.05.23, 15:53
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.getProperVolumeIcon

@Composable
fun VolumeProgressView(
    modifier: Modifier = Modifier,
    value: Float,
) {
    val volumeDisplay = (value * 100).toInt()

    Column(
        modifier = modifier
            .width(MaterialTheme.dimens.size78)
            .height(MaterialTheme.dimens.size78)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(MaterialTheme.dimens.size48),
            imageVector = getProperVolumeIcon(volumeDisplay),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = null
        )

        Text(
            text = "$volumeDisplay %",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewVolumeProgressView() {
    VideoAppTheme() {
        VolumeProgressView(value = 0.4f)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewVolumeProgressView() {
    VideoAppTheme(darkTheme = true) {
        VolumeProgressView(value = 0.8f)
    }
}