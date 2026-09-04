package com.mvproject.tinyiptvkmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.mvproject.tinyiptvkmp.di.initKoin
import com.mvproject.tinyiptvkmp.infrastructure.logging.AppLoggingConfig
import org.jetbrains.skia.Image

private const val DEBUG_LOGS_PROPERTY = "tinyiptv.debugLogs"

fun main() {

    AppLoggingConfig.applyDefaults(
        isDebug = System
            .getProperty(DEBUG_LOGS_PROPERTY)
            ?.toBooleanStrictOrNull() == true,
    )
    initKoin()

    //  System.setProperty("compose.interop.blending", "true")
    return singleWindowApplication(
        exitProcessOnExit = true,
        title = "Tiny Iptv Player",
        icon = BitmapPainter(
            useResource("drawable/tiny_iptv_logo.png") { stream ->
                Image.makeFromEncoded(stream.readBytes()).toComposeImageBitmap()
            },
        ),
        state = WindowState(
            placement = WindowPlacement.Maximized,
            height = 900.dp,
            width = 1400.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TinyIptvApp()
        }
    }
}
