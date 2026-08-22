package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

typealias DatastorePathProducer = () -> String

expect fun platformDatastorePathModule(
    fileName: String,
    pathProducerQualifier: Qualifier? = null,
): Module

const val dataStoreFileName = "tiny_iptv.preferences_pb"
