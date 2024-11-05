package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun ChannelProgramsTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = style,
        color = color,
        textAlign = TextAlign.Center,
    )
}