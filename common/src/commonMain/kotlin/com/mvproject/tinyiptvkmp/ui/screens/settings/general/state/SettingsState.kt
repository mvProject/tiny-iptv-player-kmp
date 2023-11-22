/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.general.state

import com.mvproject.tinyiptvkmp.utils.AppConstants

data class SettingsState(
    val infoUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
    val epgUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
)