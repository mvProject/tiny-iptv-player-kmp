package com.mvproject.tinyiptvkmp.core.database.builder

import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.coroutines.CoroutineContext

fun <T : RoomDatabase> createRoomDatabase(
    builder: RoomDatabase.Builder<T>,
    queryCoroutineContext: CoroutineContext = Dispatchers.IO,
): T =
    builder
        .setQueryCoroutineContext(queryCoroutineContext)
        .build()
