/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.07.24, 14:58
 *
 */

package com.mvproject.tinyiptvkmp

import androidx.compose.runtime.Composable
import coil3.compose.setSingletonImageLoaderFactory
import com.mvproject.tinyiptvkmp.core.common.utils.ImageUtils.getAsyncImageLoader
import com.mvproject.tinyiptvkmp.core.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import com.mvproject.tinyiptvkmp.navigation.NavigationHost
import org.koin.compose.KoinContext

@Composable
fun TinyIptvApp() {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    KoinContext {
        VideoAppTheme {
            NavigationHost(
                startDestination = AppRoutes.PlaylistGroup,
            )
        }
    }
}
