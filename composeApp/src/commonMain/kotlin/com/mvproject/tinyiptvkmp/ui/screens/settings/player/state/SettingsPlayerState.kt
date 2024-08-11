/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player.state

import com.mvproject.tinyiptvkmp.data.enums.RatioMode
import com.mvproject.tinyiptvkmp.data.enums.ResizeMode


data class SettingsPlayerState(
    val resizeMode: Int = ResizeMode.Fill.value,
    val ratioMode: Int = RatioMode.WideScreen.value,
    val isFullscreenEnabled: Boolean = true,
)