/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.09.23, 18:10
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.general.action

sealed class SettingsAction {
    data class SetInfoUpdatePeriod(val type: Int) : SettingsAction()
    data class SetEpgUpdatePeriod(val type: Int) : SettingsAction()
}

