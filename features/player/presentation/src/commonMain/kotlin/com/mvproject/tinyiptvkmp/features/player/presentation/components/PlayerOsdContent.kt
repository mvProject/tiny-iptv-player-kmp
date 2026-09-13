package com.mvproject.tinyiptvkmp.features.player.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelFavoriteSelector
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelProgramUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelPrograms
import com.mvproject.tinyiptvkmp.core.theme.dimensionFraction
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerAction
import com.mvproject.tinyiptvkmp.features.player.presentation.PlayerState.PlayerOSD
import com.mvproject.tinyiptvkmp.features.player.presentation.utils.programDescription
import com.mvproject.tinyiptvkmp.features.player.presentation.utils.programTitle

@Composable
fun PlayerOsdContent(
    osdType: PlayerOSD?,
    currentChannel: TvChannelWithPrograms,
    currentPrograms: List<ChannelProgramUiModel>,
    groupChannels: List<TvChannelWithPrograms>,
    currentChannelIndex: Int,
    channelGroup: String,
    overlayHeightFraction: Float,
    overlayWidthFraction: Float,
    modifier: Modifier = Modifier,
    onAction: (PlayerAction) -> Unit = {},
) {
    AnimatedContent(
        targetState = osdType,
        modifier = modifier,
        transitionSpec = {
            val enter = fadeIn() + scaleIn(initialScale = SWITCH_IN_SCALE)
            val exit = fadeOut() + scaleOut(targetScale = SWITCH_OUT_SCALE)
            enter togetherWith exit
        },
        label = "PlayerOsdContent",
    ) { targetOsdType ->
        when (targetOsdType) {
            PlayerOSD.ChannelPrograms -> {
                ChannelPrograms(
                    modifier = Modifier
                        .fillMaxHeight(overlayHeightFraction)
                        .fillMaxWidth(overlayWidthFraction)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary),
                    title = currentChannel.channelName,
                    programs = currentPrograms,
                )
            }

            PlayerOSD.GroupChannels -> {
                PlayerChannels(
                    modifier = Modifier
                        .fillMaxHeight(overlayHeightFraction)
                        .fillMaxWidth(overlayWidthFraction),
                    channels = groupChannels,
                    current = currentChannelIndex,
                    group = channelGroup,
                    onChannelSelect = { channel -> onAction(PlayerAction.SelectChannel(channel)) },
                )
            }

            PlayerOSD.ProgramInfo -> {
                ProgramInfo(
                    modifier = Modifier.fillMaxWidth(MaterialTheme.dimensionFraction.fraction80),
                    channelName = currentChannel.channelName,
                    programName = currentChannel.programTitle,
                    description = currentChannel.programDescription,
                )
            }

            PlayerOSD.ChannelFavorites -> {
                ChannelFavoriteSelector(
                    options = favoriteOptionsUiModels(currentChannel.favoriteType),
                    onSelectFavorite = { option ->
                        onAction(PlayerAction.UpdateFavorite(FavoriteType.valueOf(option.id)))
                    },
                )
            }

            null -> Unit
        }
    }
}

private const val SWITCH_IN_SCALE = 0.98f
private const val SWITCH_OUT_SCALE = 0.98f
