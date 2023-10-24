/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.10.23, 19:32
 *
 */

package com.mvproject.tinyiptv.ui.screens.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.tinyiptv.navigation.PlaylistDataRoute
import com.mvproject.tinyiptv.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = koinViewModel()

            viewModel.checkUpdates()

            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightStatusBars = !isSystemInDarkTheme()

            VideoAppTheme {
                Navigator(
                    screen = PlaylistDataRoute
                )
            }
        }
    }
}