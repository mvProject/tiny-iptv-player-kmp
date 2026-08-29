package com.mvproject.tinyiptvkmp.core.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.components.buttons.FavoriteButton
import com.mvproject.tinyiptvkmp.core.components.indicators.ProgramProgressIndicator
import com.mvproject.tinyiptvkmp.core.components.modifiers.SpacerHeight
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
fun ChannelGridView(
    channel: ChannelItemUiModel,
    modifier: Modifier = Modifier,
    onChannelSelect: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onShowEpgClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .height(140.dp)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onShowEpgClick,
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = onChannelSelect,
                    onLongClick = onShowEpgClick,
                )
                .clip(MaterialTheme.shapes.extraSmall),
        ) {
            Row(
                modifier = Modifier.padding(MaterialTheme.dimensionSize.size8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensionSize.size4),
            ) {
                ChannelLogo(
                    channelLogo = channel.logoUrl,
                    channelName = channel.name,
                )
                ChannelTitle(
                    modifier = Modifier.weight(MaterialTheme.dimensionWeight.weight1),
                    title = channel.name,
                    isFavorite = channel.isFavorite,
                    lines = 2,
                )
                FavoriteButton(
                    isFavorite = channel.isFavorite,
                    onClick = onFavoriteClick,
                )
            }

            SpacerHeight(MaterialTheme.dimensionWeight.weight1)

            val program = channel.currentProgram
            if (program == null) {
                EmptyProgramTitle(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size8),
                    title = stringResource(Res.string.msg_no_epg_found),
                )
            } else {
                ProgramTitle(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size8),
                    title = program.title,
                )
            }

            SpacerHeight(MaterialTheme.dimensionWeight.weight1)

            program?.let {
                ProgramProgressIndicator(progress = it.progress)
            }
        }
    }
}
