package com.mvproject.tinyiptvkmp.core.ui.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@Composable
fun EmptyProgramTitle(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.msg_no_epg_found)
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.outline,
    )
}