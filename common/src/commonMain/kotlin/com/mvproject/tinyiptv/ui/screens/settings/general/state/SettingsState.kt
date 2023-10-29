/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 11.09.23, 20:49
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.general.state

import com.mvproject.tinyiptv.utils.AppConstants

data class SettingsState(
    val infoUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
    val epgUpdatePeriod: Int = AppConstants.INT_VALUE_ZERO,
)