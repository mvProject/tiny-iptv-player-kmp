/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.05.23, 15:40
 *
 */

package com.mvproject.tinyiptv.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_5
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptv.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptv.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import com.mvproject.tinyiptv.utils.typeToDuration
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.days

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun setCurrentPlaylistId(playlistId: Long) {
        dataStore.edit { settings ->
            settings[SELECTED_PLAYLIST] = playlistId
        }
    }

    suspend fun loadCurrentPlaylistId() = dataStore.data.map { preferences ->
        preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
    }.first()

    val currentPlaylistId
        get() = dataStore.data.map { preferences ->
            preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
        }

    suspend fun setChannelsViewType(type: String) {
        dataStore.edit { settings ->
            settings[CHANNELS_VIEW_TYPE] = type
        }
    }

    suspend fun getChannelsViewType() = dataStore.data.map { preferences ->
        preferences[CHANNELS_VIEW_TYPE]
    }.first()

    suspend fun setEpgInfoUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_INFO_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getEpgInfoUpdatePeriod() = dataStore.data.map { preferences ->
        preferences[EPG_INFO_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
    }.first()


    suspend fun setMainEpgUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_MAIN_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getMainEpgUpdatePeriod() = dataStore.data.map { preferences ->
        preferences[EPG_MAIN_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
    }.first()

    suspend fun setDefaultResizeMode(mode: Int) {
        dataStore.edit { settings ->
            settings[DEFAULT_RESIZE_MODE] = mode
        }
    }

    suspend fun getDefaultResizeMode() = dataStore.data.map { preferences ->
        preferences[DEFAULT_RESIZE_MODE] ?: INT_VALUE_ZERO
    }.first()


    suspend fun setDefaultFullscreenMode(state: Boolean) {
        dataStore.edit { settings ->
            settings[DEFAULT_FULLSCREEN_MODE] = state
        }
    }

    suspend fun getDefaultFullscreenMode() = dataStore.data.map { preferences ->
        preferences[DEFAULT_FULLSCREEN_MODE] ?: false
    }.first()


    suspend fun setEpgInfoDataExist(state: Boolean) {
        dataStore.edit { settings ->
            settings[EPG_INFO_DATA_IS_EXIST] = state
        }
    }

    suspend fun isEpgInfoDataExist() = dataStore.data.map { preferences ->
        preferences[EPG_INFO_DATA_IS_EXIST] ?: false
    }.first()


    suspend fun setEpgInfoDataLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_INFO_DATA_LAST_UPDATE] = timestamp
        }
    }

    suspend fun isEpgInfoDataUpdateRequired() = dataStore.data.map { preferences ->
        val lastUpdate = preferences[EPG_INFO_DATA_LAST_UPDATE] ?: LONG_VALUE_ZERO
        val updatePeriod = 7.days.inWholeMilliseconds
        (actualDate - lastUpdate) > updatePeriod
    }.first()


    suspend fun setChannelsEpgInfoUpdateRequired(state: Boolean) {
        dataStore.edit { settings ->
            settings[CHANNELS_EPG_INFO_UPDATE_REQUIRED] = state
        }
    }

    fun isChannelsEpgInfoUpdateRequired() = dataStore.data.map { preferences ->
        val isUpdateRequired = preferences[CHANNELS_EPG_INFO_UPDATE_REQUIRED] ?: false
        val selectedId = preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
        if (selectedId != LONG_NO_VALUE) isUpdateRequired else false
    }

    suspend fun setEpgUnplannedUpdateRequired(state: Boolean) {
        dataStore.edit { settings ->
            settings[EPG_UNPLANNED_UPDATE_REQUIRED] = state
        }
    }

    private fun isEpgUnplannedUpdateRequired() = dataStore.data.map { preferences ->
        preferences[EPG_UNPLANNED_UPDATE_REQUIRED] ?: false
    }

    suspend fun setEpgLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_DATA_LAST_UPDATE] = timestamp
        }
    }

    private fun isEpgPlannedUpdateRequired() = dataStore.data.map { preferences ->
        val updatePeriod = preferences[EPG_MAIN_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
        val lastUpdate = preferences[EPG_DATA_LAST_UPDATE] ?: LONG_NO_VALUE
        actualDate - lastUpdate > typeToDuration(updatePeriod)
    }

    suspend fun isEpgUpdateRequired() = combine(
        isEpgUnplannedUpdateRequired(),
        isEpgPlannedUpdateRequired()
    ) { unplanned, planned ->
        return@combine unplanned || planned
    }

    private companion object {
        val SELECTED_PLAYLIST = longPreferencesKey("SelectedPlaylist")
        val CHANNELS_VIEW_TYPE = stringPreferencesKey("ChannelsViewType")

        val EPG_DATA_LAST_UPDATE = longPreferencesKey("EpgDaTaLastUpdate")

        val EPG_INFO_LAST_UPDATE_PERIOD = intPreferencesKey("EpgInfoLastUpdatePeriod")
        val EPG_MAIN_LAST_UPDATE_PERIOD = intPreferencesKey("EpgMainLastUpdatePeriod")

        val DEFAULT_RESIZE_MODE = intPreferencesKey("DefaultResizeMode")
        val DEFAULT_FULLSCREEN_MODE = booleanPreferencesKey("DefaultFullscreenMode")

        val EPG_INFO_DATA_IS_EXIST = booleanPreferencesKey("EpgInfoDataIsExist")
        val EPG_INFO_DATA_LAST_UPDATE = longPreferencesKey("EpgInfoDataIsLastUpdate")
        val CHANNELS_EPG_INFO_UPDATE_REQUIRED =
            booleanPreferencesKey("ChannelsEpgInfoUpdateRequired")
        val EPG_UNPLANNED_UPDATE_REQUIRED =
            booleanPreferencesKey("EpgUnplannedUpdateRequired")
    }
}