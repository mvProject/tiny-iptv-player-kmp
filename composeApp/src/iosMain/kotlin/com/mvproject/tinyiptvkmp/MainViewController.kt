package com.mvproject.tinyiptvkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.mvproject.tinyiptvkmp.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()

    TinyIptvApp()
}
