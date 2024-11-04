package com.mvproject.tinyiptvkmp.ui.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ChannelTitle(
    modifier: Modifier = Modifier,
    title: String,
    isFavorite: Boolean = false,
    lines: Int = 1
) {
    val color = if (isFavorite) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = lines,
        minLines = lines,
    )
}