package com.mvproject.tinyiptvkmp.core.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended

@Composable
fun ChannelTitleLarge(
    modifier: Modifier = Modifier,
    title: String,
    isFavorite: Boolean = false,
) {
    val color = if (isFavorite) {
        MaterialTheme.colorSchemeExtended.activeProgramTitle
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

@Preview
@Composable
private fun ChannelTitleLargePreview() {
    AppTheme {
        ChannelTitleLarge(
            title = "Discovery Channel",
            isFavorite = true,
        )
    }
}
