/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 09.05.24, 21:02
 *
 */

package com.mvproject.tinyiptvkmp.core.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_5
import com.mvproject.tinyiptvkmp.core.common.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptvkmp.core.common.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceRepository(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun setChannelsViewType(type: String) {
        dataStore.edit { settings ->
            settings[CHANNELS_VIEW_TYPE] = type
        }
    }

    suspend fun getChannelsViewType() =
        dataStore.data
            .map { preferences ->
                preferences[CHANNELS_VIEW_TYPE]
            }.first()

    suspend fun setEpgInfoUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_INFO_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getEpgInfoUpdatePeriod() =
        dataStore.data
            .map { preferences ->
                preferences[EPG_INFO_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
            }.first()

    suspend fun setMainEpgUpdatePeriod(type: Int) {
        dataStore.edit { settings ->
            settings[EPG_MAIN_LAST_UPDATE_PERIOD] = type
        }
    }

    suspend fun getMainEpgUpdatePeriod() =
        dataStore.data
            .map { preferences ->
                preferences[EPG_MAIN_LAST_UPDATE_PERIOD] ?: INT_VALUE_5
            }.first()

    suspend fun setDefaultResizeMode(mode: Int) {
        dataStore.edit { settings ->
            settings[DEFAULT_RESIZE_MODE] = mode
        }
    }

    suspend fun getDefaultResizeMode() =
        dataStore.data
            .map { preferences ->
                preferences[DEFAULT_RESIZE_MODE] ?: INT_VALUE_ZERO
            }.first()

    suspend fun setDefaultRatioMode(mode: Int) {
        dataStore.edit { settings ->
            settings[DEFAULT_RATIO_MODE] = mode
        }
    }

    suspend fun getDefaultRatioMode() =
        dataStore.data
            .map { preferences ->
                preferences[DEFAULT_RATIO_MODE] ?: INT_VALUE_1
            }.first()

    suspend fun setDefaultFullscreenMode(state: Boolean) {
        dataStore.edit { settings ->
            settings[DEFAULT_FULLSCREEN_MODE] = state
        }
    }

    suspend fun getDefaultFullscreenMode() =
        dataStore.data
            .map { preferences ->
                preferences[DEFAULT_FULLSCREEN_MODE] ?: false
            }.first()

    suspend fun setEpgInfoDataLastUpdate(timestamp: Long) {
        dataStore.edit { settings ->
            settings[EPG_INFO_DATA_LAST_UPDATE] = timestamp
        }
    }

    suspend fun getEpgInfoDataLastUpdate() =
        dataStore.data
            .map { preferences ->
                preferences[EPG_INFO_DATA_LAST_UPDATE] ?: LONG_VALUE_ZERO
            }.first()


    suspend fun setChannelsEpgInfoUpdateRequired(state: Boolean) {
        dataStore.edit { settings ->
            settings[CHANNELS_EPG_INFO_UPDATE_REQUIRED] = state
        }
    }

    fun isChannelsEpgInfoUpdateRequired() =
        dataStore.data.map { preferences ->
            preferences[CHANNELS_EPG_INFO_UPDATE_REQUIRED] ?: false
        }

    suspend fun setEpgLastUpdate(timestamp: Long) {
        Logger.d("testing setEpgLastUpdate timestamp $timestamp")
        dataStore.edit { settings ->
            settings[EPG_DATA_LAST_UPDATE] = timestamp
        }
    }

    suspend fun lastEpgUpdate() =
        dataStore.data
            .map { preferences ->
                preferences[EPG_DATA_LAST_UPDATE] ?: LONG_NO_VALUE
            }.first()

    suspend fun setIdForPlaylistContentLoad(id: String) {
        dataStore.edit { settings ->
            settings[PLAYLIST_CONTENT_LOAD_REQUIRED] = id
        }
    }

    fun idForPlaylistContentLoad() =
        dataStore.data.map { preferences ->
            preferences[PLAYLIST_CONTENT_LOAD_REQUIRED] ?: String.empty
        }

    /**
     * Sets the time for cleaning programs.
     *
     * @param timeInMillis The clean time in milliseconds.
     */
    suspend fun setProgramsCleanTime(timeInMillis: Long) {
        dataStore.edit { settings ->
            settings[EPG_PROGRAM_CLEAN] = timeInMillis
        }
    }

    /**
     * Gets the time for cleaning programs.
     *
     * @return The clean time in milliseconds.
     */
    suspend fun getProgramsCleanTime() =
        dataStore.data
            .map { preferences ->
                preferences[EPG_PROGRAM_CLEAN] ?: LONG_NO_VALUE
            }.first()

    private companion object {
        val CHANNELS_VIEW_TYPE = stringPreferencesKey("ChannelsViewType")

        val EPG_DATA_LAST_UPDATE = longPreferencesKey("EpgDaTaLastUpdate")

        val EPG_INFO_LAST_UPDATE_PERIOD = intPreferencesKey("EpgInfoLastUpdatePeriod")
        val EPG_MAIN_LAST_UPDATE_PERIOD = intPreferencesKey("EpgMainLastUpdatePeriod")

        val DEFAULT_RESIZE_MODE = intPreferencesKey("DefaultResizeMode")
        val DEFAULT_RATIO_MODE = intPreferencesKey("DefaultRatioMode")
        val DEFAULT_FULLSCREEN_MODE = booleanPreferencesKey("DefaultFullscreenMode")

        val EPG_INFO_DATA_LAST_UPDATE = longPreferencesKey("EpgInfoDataIsLastUpdate")
        val CHANNELS_EPG_INFO_UPDATE_REQUIRED =
            booleanPreferencesKey("ChannelsEpgInfoUpdateRequired")

        val PLAYLIST_CONTENT_LOAD_REQUIRED = stringPreferencesKey("PlaylistContentLoadRequired")
        val EPG_PROGRAM_CLEAN = longPreferencesKey("epgProgramClean")
    }
}
