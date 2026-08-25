package com.mvproject.tinyiptvkmp.core.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended

@Composable
fun EmptyProgramTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorSchemeExtended.emptyProgramTitle,
    )
}

@Preview
@Composable
private fun EmptyProgramTitlePreview() {
    AppTheme {
        EmptyProgramTitle(title = "epg not found")
    }
}
