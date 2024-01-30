/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 30.01.24, 14:57
 *
 */

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.mvproject.tinyiptvkmp.TinyIptvApp
import com.mvproject.tinyiptvkmp.di.initKoin

fun main() {

    initKoin()

    return singleWindowApplication(
        exitProcessOnExit = true,
        title = "Tiny Iptv Player",
        icon = BitmapPainter(useResource("drawable/tiny_iptv_logo.png", ::loadImageBitmap)),
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