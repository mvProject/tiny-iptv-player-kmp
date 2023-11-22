/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptvkmp.data.enums

import com.mvproject.tinyiptvkmp.MainRes

enum class UpdatePeriod(val value: Int, val title: String) {
    NO_UPDATE(0, MainRes.string.playlist_update_never),
    HOURS_6(1, MainRes.string.playlist_update_6_hours),
    HOURS_12(2, MainRes.string.playlist_update_12_hours),
    HOURS_24(3, MainRes.string.playlist_update_24_hours),
    DAYS_2(4, MainRes.string.playlist_update_2_days),
    WEEK_1(5, MainRes.string.playlist_update_1_week)
}