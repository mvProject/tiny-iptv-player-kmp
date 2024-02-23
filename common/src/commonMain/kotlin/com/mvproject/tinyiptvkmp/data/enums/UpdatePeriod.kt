/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 30.01.24, 13:56
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.mvproject.tinyiptvkmp.data.enums

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import tinyiptvkmp.common.generated.resources.Res

enum class UpdatePeriod(val value: Int, val title: StringResource) {
    NO_UPDATE(0, Res.string.playlist_update_never),
    HOURS_6(1, Res.string.playlist_update_6_hours),
    HOURS_12(2, Res.string.playlist_update_12_hours),
    HOURS_24(3, Res.string.playlist_update_24_hours),
    DAYS_2(4, Res.string.playlist_update_2_days),
    WEEK_1(5, Res.string.playlist_update_1_week)
}