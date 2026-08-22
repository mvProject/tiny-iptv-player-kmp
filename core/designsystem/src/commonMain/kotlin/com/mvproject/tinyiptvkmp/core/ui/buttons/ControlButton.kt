package com.mvproject.tinyiptvkmp.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme

@Composable
fun ControlButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.AutoMirrored.Rounded.ViewList,
    onClick: () -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = imageVector.name,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
private fun ControlButtonPreview() {
    AppTheme {
        ControlButton()
    }
}
