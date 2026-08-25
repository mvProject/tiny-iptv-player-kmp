package com.mvproject.tinyiptvkmp.core.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@Composable
fun ActionButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ElevatedButton(
        onClick = onClick,
        modifier =
            modifier
                .padding(MaterialTheme.dimensionSize.size8)
                .fillMaxWidth(),
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
