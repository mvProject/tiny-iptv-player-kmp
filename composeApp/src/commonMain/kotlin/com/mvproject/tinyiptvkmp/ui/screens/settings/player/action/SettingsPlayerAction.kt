/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptvkmp.ui.screens.settings.player.action

sealed class SettingsPlayerAction {
    data class SetResizeMode(val mode: Int) : SettingsPlayerAction()
    data class SetRatioMode(val mode: Int) : SettingsPlayerAction()
    data class SetFullScreenMode(val state: Boolean) : SettingsPlayerAction()
}

