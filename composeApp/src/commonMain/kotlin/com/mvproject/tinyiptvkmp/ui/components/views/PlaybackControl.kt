/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 17.05.24, 18:23
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PlaybackControl(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.AutoMirrored.Rounded.ViewList,
    action: () -> Unit,
) {
    /*    Icon(
            modifier =
                modifier
                    .clip(CircleShape)
                    .clickable {
                        action()
                    }
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(MaterialTheme.dimens.size8),
            imageVector = imageVector,
            contentDescription = "PlaybackControl",
            tint = MaterialTheme.colorScheme.primary,
        )*/

    IconButton(
        modifier = modifier,
        onClick = action,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "PlaybackControl",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
