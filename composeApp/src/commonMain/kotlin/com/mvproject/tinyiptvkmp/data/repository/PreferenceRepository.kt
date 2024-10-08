/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 09.05.24, 21:02
 *
 */

package com.mvproject.tinyiptvkmp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_5
import com.mvproject.tinyiptvkmp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.utils.KLog
import com.mvproject.tinyiptvkmp.utils.TimeUtils.actualDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.days

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun setCurrentPlaylistId(playlistId: Long) {
        dataStore.edit { settings ->
            settings[SELECTED_PLAYLIST] = playlistId
        }
    }

    suspend fun loadCurrentPlaylistId() =
        dataStore.data.map { preferences ->
            preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
        }.first()

    val currentPlaylistId
        get() =
            dataStore.data.map { preferences ->
                preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
            }

    suspend fun setChannelsViewType(type: String) {
        dataStore.edit { settings ->
            settings[CHANNELS_VIEW_TYPE] = type
        }
    }

    suspend fun getChannelsViewType() =
        dataStore.data.map { preferences ->
            preferences[CHANNELS_VIEW_TYPE]
        }.first()

    suspend fun setEpgInfoUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_INFO_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getEpgInfoUpdatePeriod() =
        dataStore.data.map { preferences ->
            preferences[EPG_INFO_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
        }.first()

    suspend fun setMainEpgUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_MAIN_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getMainEpgUpdatePeriod() =
        dataStore.data.map { preferences ->
            preferences[EPG_MAIN_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
        }.first()

    suspend fun setDefaultResizeMode(mode: Int) {
        dataStore.edit { settings ->
            settings[DEFAULT_RESIZE_MODE] = mode
        }
    }

    suspend fun getDefaultResizeMode() =
        dataStore.data.map { preferences ->
            preferences[DEFAULT_RESIZE_MODE] ?: INT_VALUE_ZERO
        }.first()

    suspend fun setDefaultRatioMode(mode: Int) {
        dataStore.edit { settings ->
            settings[DEFAULT_RATIO_MODE] = mode
        }
    }

    suspend fun getDefaultRatioMode() =
        dataStore.data.map { preferences ->
            preferences[DEFAULT_RATIO_MODE] ?: INT_VALUE_1
        }.first()

    suspend fun setDefaultFullscreenMode(state: Boolean) {
        dataStore.edit { settings ->
            settings[DEFAULT_FULLSCREEN_MODE] = state
        }
    }

    suspend fun getDefaultFullscreenMode() =
        dataStore.data.map { preferences ->
            preferences[DEFAULT_FULLSCREEN_MODE] ?: false
        }.first()

    suspend fun setEpgInfoDataExist(state: Boolean) {
        dataStore.edit { settings ->
            settings[EPG_INFO_DATA_IS_EXIST] = state
        }
    }

    fun isEpgInfoDataExist() =
        dataStore.data.map { preferences ->
            preferences[EPG_INFO_DATA_IS_EXIST] ?: false
        }

    suspend fun isEpgInfoDataExist2() =
        dataStore.data.map { preferences ->
            preferences[EPG_INFO_DATA_IS_EXIST] ?: false
        }.first()

    suspend fun setEpgInfoDataLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_INFO_DATA_LAST_UPDATE] = timestamp
        }
    }

    suspend fun isEpgInfoDataUpdateRequired() =
        dataStore.data.map { preferences ->
            val lastUpdate = preferences[EPG_INFO_DATA_LAST_UPDATE] ?: LONG_VALUE_ZERO
            val updatePeriod = 7.days.inWholeMilliseconds
            (actualDate - lastUpdate) > updatePeriod
        }.first()

    suspend fun setChannelsEpgInfoUpdateRequired(state: Boolean) {
        dataStore.edit { settings ->
            settings[CHANNELS_EPG_INFO_UPDATE_REQUIRED] = state
        }
    }

    fun isChannelsEpgInfoUpdateRequired() =
        dataStore.data.map { preferences ->
            val isUpdateRequired = preferences[CHANNELS_EPG_INFO_UPDATE_REQUIRED] ?: false
            val selectedId = preferences[SELECTED_PLAYLIST] ?: LONG_NO_VALUE
            if (selectedId != LONG_NO_VALUE) isUpdateRequired else false
        }

    suspend fun setEpgLastUpdate(timestamp: Long) {
        KLog.d("testing setEpgLastUpdate timestamp $timestamp")
        dataStore.edit { settings ->
            settings[EPG_DATA_LAST_UPDATE] = timestamp
        }
    }

    fun lastEpgUpdate() =
        dataStore.data.map { preferences ->
            preferences[EPG_DATA_LAST_UPDATE] ?: LONG_NO_VALUE
        }

    fun epgUpdatePeriod() =
        dataStore.data.map { preferences ->
            preferences[EPG_MAIN_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
        }

    private companion object {
        val SELECTED_PLAYLIST = longPreferencesKey("SelectedPlaylist")
        val CHANNELS_VIEW_TYPE = stringPreferencesKey("ChannelsViewType")

        val EPG_DATA_LAST_UPDATE = longPreferencesKey("EpgDaTaLastUpdate")

        val EPG_INFO_LAST_UPDATE_PERIOD = intPreferencesKey("EpgInfoLastUpdatePeriod")
        val EPG_MAIN_LAST_UPDATE_PERIOD = intPreferencesKey("EpgMainLastUpdatePeriod")

        val DEFAULT_RESIZE_MODE = intPreferencesKey("DefaultResizeMode")
        val DEFAULT_RATIO_MODE = intPreferencesKey("DefaultRatioMode")
        val DEFAULT_FULLSCREEN_MODE = booleanPreferencesKey("DefaultFullscreenMode")

        val EPG_INFO_DATA_IS_EXIST = booleanPreferencesKey("EpgInfoDataIsExist")
        val EPG_INFO_DATA_LAST_UPDATE = longPreferencesKey("EpgInfoDataIsLastUpdate")
        val CHANNELS_EPG_INFO_UPDATE_REQUIRED =
            booleanPreferencesKey("ChannelsEpgInfoUpdateRequired")
    }
}
