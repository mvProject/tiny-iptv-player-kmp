package com.mvproject.tinyiptvkmp.core.foundation.model

import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType.Companion.mapViewType
import kotlin.test.Test
import kotlin.test.assertEquals

class FoundationModelTest {
    @Test
    fun mapViewTypeReturnsListForNullValue() {
        assertEquals(ChannelsViewType.LIST, null.mapViewType())
    }

    @Test
    fun mapViewTypeReturnsStoredValue() {
        assertEquals(ChannelsViewType.GRID, "GRID".mapViewType())
    }

    @Test
    fun mapViewTypeReturnsListForBlankValue() {
        assertEquals(ChannelsViewType.LIST, "".mapViewType())
    }

    @Test
    fun mapViewTypeReturnsListForUnknownValue() {
        assertEquals(ChannelsViewType.LIST, "UNKNOWN".mapViewType())
    }

    @Test
    fun toggleVideoSizeCyclesThroughAllModes() {
        assertEquals(VideoSize.FitScreen, VideoSize.toggleVideoSize(VideoSize.Cinematic))
        assertEquals(VideoSize.WideScreen, VideoSize.toggleVideoSize(VideoSize.FitScreen))
        assertEquals(VideoSize.FillScreen, VideoSize.toggleVideoSize(VideoSize.WideScreen))
        assertEquals(VideoSize.FullScreen, VideoSize.toggleVideoSize(VideoSize.FillScreen))
        assertEquals(VideoSize.Cinematic, VideoSize.toggleVideoSize(VideoSize.FullScreen))
    }

    @Test
    fun updatePeriodValuesMatchPersistedIndexes() {
        assertEquals(0, UpdatePeriod.NO_UPDATE.value)
        assertEquals(1, UpdatePeriod.HOURS_6.value)
        assertEquals(2, UpdatePeriod.HOURS_12.value)
        assertEquals(3, UpdatePeriod.HOURS_24.value)
        assertEquals(4, UpdatePeriod.DAYS_2.value)
        assertEquals(5, UpdatePeriod.WEEK_1.value)
    }
}
