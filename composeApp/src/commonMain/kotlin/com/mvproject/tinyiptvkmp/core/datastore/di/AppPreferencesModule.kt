package com.mvproject.tinyiptvkmp.core.datastore.di

import com.mvproject.tinyiptvkmp.core.datastore.DataStorePathProvider
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStore
import com.mvproject.tinyiptvkmp.core.datastore.ProtoStoreFactory
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesProto
import com.mvproject.tinyiptvkmp.core.datastore.preferences.AppPreferencesSerializer
import org.koin.dsl.module

private const val APP_PREFERENCES_FILE_NAME = "tiny_iptv_settings.pb"

val appPreferencesModule =
    module {
        single<ProtoStore<AppPreferencesProto>> {
            ProtoStoreFactory.create(
                serializer = AppPreferencesSerializer,
                producePath = {
                    get<DataStorePathProvider>().providePath(APP_PREFERENCES_FILE_NAME)
                },
            )
        }
    }
