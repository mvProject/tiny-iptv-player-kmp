/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 17:47
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.action

import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel

sealed class GroupChannelsAction {
    data class ToggleFavourites(
        val channel: TvChannel,
        val type: FavoriteType,
    ) : GroupChannelsAction()

    data class SearchTextChange(
        val text: String,
    ) : GroupChannelsAction()

    data class ViewTypeChange(
        val type: ChannelsViewType,
    ) : GroupChannelsAction()

    data class ToggleEpgVisibility(
        val name: String = String.empty,
        val epgID: String = String.empty
    ) : GroupChannelsAction()
}
