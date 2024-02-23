/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 23.02.24, 11:07
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
fun PlaybackControl(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.AutoMirrored.Rounded.ViewList,
    action: () -> Unit
) {
    Icon(
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                action()
            }
            .background(MaterialTheme.colorScheme.onSurface)
            .padding(MaterialTheme.dimens.size8),
        imageVector = imageVector,
        contentDescription = "PlaybackControl",
        tint = MaterialTheme.colorScheme.primary
    )
}