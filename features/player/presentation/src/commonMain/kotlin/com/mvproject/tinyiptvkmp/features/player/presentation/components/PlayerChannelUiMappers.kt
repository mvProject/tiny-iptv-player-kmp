package com.mvproject.tinyiptvkmp.features.player.presentation.components

import androidx.compose.runtime.Composable
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelItemUiModel
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelProgramUiModel
import com.mvproject.tinyiptvkmp.core.components.selectors.OptionSet
import com.mvproject.tinyiptvkmp.core.components.selectors.optionSetOf
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_cartoon
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_common
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_documentary
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_movie
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_show
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.favorite_sport
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res as DesignSystemRes

internal fun TvChannelWithPrograms.toChannelItemUiModel() =
    ChannelItemUiModel(
        id = "$channelName$channelUrl",
        name = channelName,
        logoUrl = channelLogo,
        isFavorite = favoriteType != FavoriteType.NONE,
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

@Composable
internal fun favoriteOptionSet(favoriteType: FavoriteType): OptionSet<FavoriteType> =
    optionSetOf(
        values = FavoriteType.entries.filter { it != FavoriteType.NONE },
        label = { type -> type.labelRes()?.let { stringResource(it) } ?: type.name },
    )

/** `null` for [FavoriteType.NONE], which is never offered as a filter. */
private fun FavoriteType.labelRes(): StringResource? =
    when (this) {
        FavoriteType.COMMON -> DesignSystemRes.string.favorite_common
        FavoriteType.MOVIE -> DesignSystemRes.string.favorite_movie
        FavoriteType.SHOW -> DesignSystemRes.string.favorite_show
        FavoriteType.SPORT -> DesignSystemRes.string.favorite_sport
        FavoriteType.CARTOON -> DesignSystemRes.string.favorite_cartoon
        FavoriteType.DOCUMENTAL -> DesignSystemRes.string.favorite_documentary
        FavoriteType.NONE -> null
    }
