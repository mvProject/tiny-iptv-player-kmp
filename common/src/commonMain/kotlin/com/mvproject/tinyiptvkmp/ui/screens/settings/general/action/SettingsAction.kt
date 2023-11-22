/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.general.action

sealed class SettingsAction {
    data class SetInfoUpdatePeriod(val type: Int) : SettingsAction()
    data class SetEpgUpdatePeriod(val type: Int) : SettingsAction()
}

