/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 15:37
 *
 */

package com.mvproject.tinyiptvkmp.core.domain.enums

enum class ChannelsViewType {
    LIST,
    GRID,
    CARD;

    companion object {
        fun String?.mapViewType() = this?.let { ChannelsViewType.valueOf(it) } ?: LIST
    }
}