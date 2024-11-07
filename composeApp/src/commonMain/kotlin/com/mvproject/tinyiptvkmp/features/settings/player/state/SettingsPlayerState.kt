/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.features.settings.player.state

import com.mvproject.tinyiptvkmp.core.domain.enums.RatioMode
import com.mvproject.tinyiptvkmp.core.domain.enums.ResizeMode


data class SettingsPlayerState(
    val resizeMode: Int = ResizeMode.Fill.value,
    val ratioMode: Int = RatioMode.WideScreen.value,
    val isFullscreenEnabled: Boolean = true,
)