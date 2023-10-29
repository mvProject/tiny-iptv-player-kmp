/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 11.09.23, 20:47
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.player.state

import com.mvproject.tinyiptv.data.enums.ResizeMode

data class SettingsPlayerState(
    val resizeMode: Int = ResizeMode.Fill.value,
    val isFullscreenEnabled: Boolean = true,
)