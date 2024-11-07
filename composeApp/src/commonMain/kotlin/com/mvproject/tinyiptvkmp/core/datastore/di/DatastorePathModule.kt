package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.core.module.Module

expect fun datastorePathModule(): Module

internal const val dataStoreFileName = "tiny_iptv.preferences_pb"
