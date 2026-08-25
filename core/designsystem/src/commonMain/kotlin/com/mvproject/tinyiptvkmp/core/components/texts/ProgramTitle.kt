package com.mvproject.tinyiptvkmp.core.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended

@Composable
fun ProgramTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorSchemeExtended.programTitle,
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
        minLines = 1,
    )
}

@Preview
@Composable
private fun ProgramTitlePreview() {
    AppTheme {
        ProgramTitle(title = "Live news and weather update")
    }
}
