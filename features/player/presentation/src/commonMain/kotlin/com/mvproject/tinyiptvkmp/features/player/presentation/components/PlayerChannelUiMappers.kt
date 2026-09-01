package com.mvproject.tinyiptvkmp.features.player.presentation.components

import com.mvproject.tinyiptvkmp.core.components.channels.ChannelItemUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelProgramUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.FavoriteOptionUiModel
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.FavoriteType

internal fun TvChannelWithPrograms.toChannelItemUiModel() =
    ChannelItemUiModel(
        id = "$channelName$channelUrl",
        name = channelName,
        logoUrl = channelLogo,
        isFavorite = favoriteType != FavoriteType.NONE.name,
        currentProgram = programs.firstOrNull()?.toChannelProgramUiModel(),
    )

internal fun EpgProgram.toChannelProgramUiModel() =
    ChannelProgramUiModel(
        id = programId,
        title = title,
        startMillis = dateTimeStart,
        endMillis = dateTimeEnd,
        progress = programProgress,
    )

internal fun favoriteOptionsUiModels(favoriteType: String) =
    FavoriteType.entries
        .filter { it != FavoriteType.NONE }
        .map { favorite ->
            FavoriteOptionUiModel(
                id = favorite.name,
                label = favorite.name,
                isSelected = favoriteType == favorite.name,
            )
        }
