package com.mvproject.tinyiptvkmp.core.datastore

import android.content.Context

actual class DataStorePathProvider(
    private val context: Context,
) {
    actual fun providePath(fileName: String): String =
        context.filesDir.resolve(fileName).absolutePath
}
