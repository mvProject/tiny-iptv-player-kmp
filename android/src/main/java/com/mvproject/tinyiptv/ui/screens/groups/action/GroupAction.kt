/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.09.23, 12:23
 *
 */

package com.mvproject.tinyiptv.ui.screens.groups.action

sealed class GroupAction {
    data class SelectPlaylist(val id: Int) : GroupAction()
}

