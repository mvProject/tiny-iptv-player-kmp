/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 15:49
 *
 */

package com.mvproject.tinyiptvkmp.core.ui.components.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mvproject.tinyiptvkmp.core.ui.theme.dimens

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

@Composable
@NonRestartableComposable
fun ColumnScope.SpacerHeight(height: Dp) {
    Spacer(modifier = Modifier.height(height = height))
}

@Composable
@NonRestartableComposable
fun ColumnScope.SpacerHeight(weight: Float) {
    Spacer(modifier = Modifier.weight(weight = weight))
}

@Composable
@NonRestartableComposable
fun RowScope.SpacerWidth(width: Dp) {
    Spacer(modifier = Modifier.width(width = width))
}

@Composable
@NonRestartableComposable
fun RowScope.SpacerWidth(weight: Float) {
    Spacer(modifier = Modifier.weight(weight = weight))
}
