package com.mvproject.tinyiptvkmp.ui.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ProgramTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    lines: Int = 2
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = style,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = lines,
        minLines = lines,
    )
}