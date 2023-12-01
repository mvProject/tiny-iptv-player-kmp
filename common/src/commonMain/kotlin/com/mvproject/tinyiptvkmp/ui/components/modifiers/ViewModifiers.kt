/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 15:49
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants

fun Modifier.fullScreenWidth(
    enabled: Boolean
) = this then Modifier.fillMaxWidth(
    if (enabled)
        AppConstants.WEIGHT_50
    else AppConstants.WEIGHT_80
)

@Composable
fun Modifier.roundedHeader(
    color: Color = MaterialTheme.colorScheme.onSurface,
    size: Dp = MaterialTheme.dimens.size8,
    padding: Dp = MaterialTheme.dimens.size8
) = this then Modifier
    .background(
        color = color,
        shape = RoundedCornerShape(
            topStart = size,
            topEnd = size
        )
    )
    .padding(padding)
