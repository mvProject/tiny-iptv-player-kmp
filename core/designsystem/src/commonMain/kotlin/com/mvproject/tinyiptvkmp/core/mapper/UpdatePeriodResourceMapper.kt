package com.mvproject.tinyiptvkmp.core.mapper

import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.Res
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.playlist_update_12_hours
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.playlist_update_1_week
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.playlist_update_24_hours
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.playlist_update_2_days
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.playlist_update_6_hours
import com.mvproject.tinyiptvkmp.core.designsystem.generated.resources.playlist_update_never
import com.mvproject.tinyiptvkmp.core.foundation.model.UpdatePeriod

fun UpdatePeriod.mapToString() =
    when (this) {
        UpdatePeriod.NO_UPDATE -> Res.string.playlist_update_never
        UpdatePeriod.HOURS_6 -> Res.string.playlist_update_6_hours
        UpdatePeriod.HOURS_12 -> Res.string.playlist_update_12_hours
        UpdatePeriod.HOURS_24 -> Res.string.playlist_update_24_hours
        UpdatePeriod.DAYS_2 -> Res.string.playlist_update_2_days
        UpdatePeriod.WEEK_1 -> Res.string.playlist_update_1_week
    }
