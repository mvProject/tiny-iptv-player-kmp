package com.mvproject.tinyiptvkmp.core.datastore

import okio.FileSystem

internal actual fun systemFileSystem(): FileSystem = FileSystem.SYSTEM
