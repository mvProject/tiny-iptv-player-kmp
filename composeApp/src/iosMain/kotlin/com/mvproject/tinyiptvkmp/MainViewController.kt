package com.mvproject.tinyiptvkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.mvproject.tinyiptvkmp.di.initKoin
import org.koin.compose.KoinContext

fun MainViewController() = ComposeUIViewController {
    initKoin()

    KoinContext {
        TinyIptvApp()
    }
}