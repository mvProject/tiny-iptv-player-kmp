package com.mvproject.tinyiptvkmp.core.datastore

import java.io.File

actual class DataStorePathProvider {
    actual fun providePath(fileName: String): String {
        val appDirectory = File(System.getProperty("user.home"), ".tinyiptv").apply {
            mkdirs()
        }
        return File(appDirectory, fileName).absolutePath
    }
}
