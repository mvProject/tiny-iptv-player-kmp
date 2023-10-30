/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.PlayerUtils.getProperVolumeIcon

@Composable
fun VolumeProgressView(
    isVisible: Boolean = false,
    modifier: Modifier = Modifier,
    value: Float,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            val volumeDisplay = (value * 100).toInt()

            Column(
                modifier = Modifier
                    .width(MaterialTheme.dimens.size78)
                    .height(MaterialTheme.dimens.size78)
                    .background(
                        MaterialTheme.colorScheme.primary,
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
    }

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
