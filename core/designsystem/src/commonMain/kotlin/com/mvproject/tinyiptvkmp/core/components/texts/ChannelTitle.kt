package com.mvproject.tinyiptvkmp.core.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended

@Composable
fun ChannelTitle(
    title: String,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    lines: Int = 1
) {
    val color = if (isFavorite) {
        MaterialTheme.colorSchemeExtended.activeProgramTitle
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
        minLines = 1,
    )
}

@Preview
@Composable
private fun ChannelTitlePreview() {
    AppTheme {
        ChannelTitle(
            title = "Discovery Channel",
            isFavorite = true,
        )
    }
}
