package com.mvproject.tinyiptvkmp.core.common.utils

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import okio.FileSystem

object ImageUtils {
    fun getAsyncImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader
            .Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                createMemoryCache(context)
            }.diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                createDiskCache()
            }.crossfade(true)
            .logger(DebugLogger())
            .build()

    private fun createMemoryCache(context: PlatformContext): MemoryCache =
        MemoryCache
            .Builder()
            .maxSizePercent(context, 0.3)
            .strongReferencesEnabled(true)
            .build()

    private fun createDiskCache(): DiskCache =
        DiskCache
            .Builder()
            .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
            .maxSizePercent(0.02)
            .build()
}
