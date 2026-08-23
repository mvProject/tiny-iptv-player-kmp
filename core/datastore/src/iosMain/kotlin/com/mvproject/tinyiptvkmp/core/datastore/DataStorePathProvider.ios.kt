package com.mvproject.tinyiptvkmp.core.datastore

import platform.Foundation.NSHomeDirectory

actual class DataStorePathProvider {
    actual fun providePath(fileName: String): String =
        "${NSHomeDirectory()}/Documents/$fileName"
}
