package com.mvproject.tinyiptvkmp.core.datastore

expect class DataStorePathProvider {
    fun providePath(fileName: String): String
}
