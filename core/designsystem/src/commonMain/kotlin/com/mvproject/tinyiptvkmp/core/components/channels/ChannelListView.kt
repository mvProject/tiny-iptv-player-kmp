package com.mvproject.tinyiptvkmp.core.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.core.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.components.texts.ChannelTitle
import com.mvproject.tinyiptvkmp.core.components.texts.EmptyProgramTitle
import com.mvproject.tinyiptvkmp.core.components.texts.ProgramTitle
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.msg_no_epg_found
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionWeight
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelListView(
    channel: ChannelItemUiModel,
    modifier: Modifier = Modifier,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onShowEpgClick,
            )
            .clip(MaterialTheme.shapes.extraSmall),
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimensionSize.size8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size8),
        ) {
            ChannelLogo(
                channelLogo = channel.logoUrl,
                channelName = channel.name,
            )
            Column(
                modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
            ) {
                ChannelTitle(
                    title = channel.name,
                    isFavorite = channel.isFavorite,
                )
                val program = channel.currentProgram
                if (program == null) {
                    EmptyProgramTitle(title = stringResource(Res.string.msg_no_epg_found))
                } else {
                    ProgramTitle(title = program.title)
                }
            }
            FavoriteButton(
                isFavorite = channel.isFavorite,
                onClick = onFavoriteClick,
            )
        }

        channel.currentProgram?.let { program ->
            ProgramProgressIndicator(progress = program.progress)
        }
    }
}
