package com.mvproject.tinyiptvkmp.core.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme

@Composable
fun SelectButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors =
            ButtonDefaults
                .buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
@Preview
private fun SelectButtonPreview() {
    AppTheme {
        SelectButton(
            title = "Current playlist",
        )
    }
}
