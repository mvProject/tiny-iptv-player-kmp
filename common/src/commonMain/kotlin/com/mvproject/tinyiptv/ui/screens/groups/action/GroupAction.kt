/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
 *
 */

package com.mvproject.tinyiptv.ui.screens.groups.action

sealed class GroupAction {
    data class SelectPlaylist(val id: Int) : GroupAction()
}

