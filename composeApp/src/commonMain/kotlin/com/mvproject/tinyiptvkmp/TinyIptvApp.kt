/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 14:58
 *
 */

package com.mvproject.tinyiptvkmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLoggingConfig
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavigationHost
import okio.FileSystem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TinyIptvApp() {
    val rootViewModel = koinViewModel<RootViewModel>()

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    LaunchedEffect(rootViewModel) {
        rootViewModel.refreshRemotePlaylistContentOnStart()
    }

    AppTheme {
        NavigationHost(
            startDestination = AppRoutes.PlaylistGroup,
        )
    }
}

private fun getAsyncImageLoader(context: PlatformContext): ImageLoader =
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
        .apply {
            if (AppLoggingConfig.isImageDebugLoggingEnabled) {
                logger(DebugLogger())
            }
        }
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
