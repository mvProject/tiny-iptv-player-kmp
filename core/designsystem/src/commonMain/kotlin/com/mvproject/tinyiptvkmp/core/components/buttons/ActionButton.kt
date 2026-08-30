package com.mvproject.tinyiptvkmp.core.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme

@Composable
fun ActionButton(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    ElevatedButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults
                .buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
@Preview
private fun ActionButtonPreview() {
    AppTheme {
        ActionButton(
            title = "Save playlist",
        )
    }
}
