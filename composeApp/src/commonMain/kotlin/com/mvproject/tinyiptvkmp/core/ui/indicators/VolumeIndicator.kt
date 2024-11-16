/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.ui.indicators

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeDown
import androidx.compose.material.icons.automirrored.rounded.VolumeMute
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun VolumeIndicator(
    isVisible: Boolean = false,
    modifier: Modifier = Modifier,
    value: Float,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            val volumeDisplay = (value * 100).toInt()

            Column(
                modifier = Modifier
                    .width(MaterialTheme.dimensionSize.size78)
                    .height(MaterialTheme.dimensionSize.size78)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    modifier = Modifier.size(MaterialTheme.dimensionSize.size48),
                    imageVector = getProperVolumeIcon(volumeDisplay),
                    tint = MaterialTheme.colorSchemeExtended.volumeIndicator,
                    contentDescription = null
                )

                Text(
                    text = "$volumeDisplay %",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorSchemeExtended.volumeIndicator
                )
            }
        }
    }

}

private fun getProperVolumeIcon(value: Int) = when {
    value < 35 -> Icons.AutoMirrored.Rounded.VolumeMute
    value > 65 -> Icons.AutoMirrored.Rounded.VolumeUp
    else -> Icons.AutoMirrored.Rounded.VolumeDown
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewVolumeProgressView() {
    VideoAppTheme(darkTheme = true) {
        VolumeProgressView(value = 0.8f)
    }
}*/
