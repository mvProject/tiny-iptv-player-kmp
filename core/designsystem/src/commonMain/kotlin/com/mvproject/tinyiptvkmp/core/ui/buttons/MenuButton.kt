package com.mvproject.tinyiptvkmp.core.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.Settings,
    onClick: () -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = imageVector.name,
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
@Preview
private fun MenuButtonPreview() {
    AppTheme {
        MenuButton()
    }
}
