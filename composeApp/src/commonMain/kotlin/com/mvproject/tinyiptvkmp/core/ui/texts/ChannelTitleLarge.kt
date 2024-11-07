package com.mvproject.tinyiptvkmp.core.ui.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ChannelTitleLarge(
    modifier: Modifier = Modifier,
    title: String,
    isFavorite: Boolean = false,
) {
    val color = if (isFavorite) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        textAlign = TextAlign.Center
    )
}