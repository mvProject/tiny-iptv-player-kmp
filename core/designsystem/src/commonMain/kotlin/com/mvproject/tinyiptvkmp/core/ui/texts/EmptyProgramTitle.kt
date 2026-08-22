package com.mvproject.tinyiptvkmp.core.ui.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import org.jetbrains.compose.ui.tooling.preview.Preview

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
