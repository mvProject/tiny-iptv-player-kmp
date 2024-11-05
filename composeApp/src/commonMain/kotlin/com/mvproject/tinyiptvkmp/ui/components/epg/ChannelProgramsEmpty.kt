package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

@Composable
internal fun ChannelProgramsEmpty(
    modifier: Modifier = Modifier,
    title: String = String.empty
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.size12),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}