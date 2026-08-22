package com.mvproject.tinyiptvkmp.features.player.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.UIKitView
import com.mvproject.tinyiptvkmp.features.player.PlayerUiAction
import com.mvproject.tinyiptvkmp.features.player.PlayerUiState
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.component.KoinComponent
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVKit.AVPlayerViewController
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView

private object PlayerViewLogger : KoinComponent {
    val logger by injectLogger("PlayerView")
}

@Composable
actual fun PlayerView(
    modifier: Modifier,
    uiState: PlayerUiState,
    onAction: (PlayerUiAction) -> Unit,
) {
    VideoPlayer(
        modifier = modifier.fillMaxSize(),
        url = uiState.currentChannel.channelUrl
    )
}

@OptIn(ExperimentalForeignApi::class)
@Composable
fun VideoPlayer(modifier: Modifier, url: String) {
    val density = LocalDensity.current

    val avPlayer = remember { AVPlayer() }

    val avPlayerLayer = remember {
        AVPlayerLayer().apply {
            player = avPlayer
            videoGravity = AVLayerVideoGravityResize
        }
    }
    val avPlayerViewController = remember {
        AVPlayerViewController().apply {
            player = avPlayer
            showsPlaybackControls = false
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val x = with(density) { maxWidth.toPx() }
        val y = with(density) { maxHeight.toPx() }

        UIKitView(
            modifier = modifier.fillMaxSize(),
            factory = {
                UIView().apply {
                    addSubview(avPlayerViewController.view)
                }
            },
            update = { view ->
                val rect = CGRectMake(
                    0.0, 0.0,
                    x.toDouble(), y.toDouble()
                )

                CATransaction.apply {
                    begin()
                    setValue(true, kCATransactionDisableActions)
                    view.layer.setFrame(rect)
                    avPlayerLayer.setFrame(rect)
                    avPlayerViewController.view.layer.frame = rect
                    commit()
                }
            }
        )
    }

    DisposableEffect(url) {
        NSURL.URLWithString(url)?.let { url ->
            avPlayerViewController.player?.apply {
                replaceCurrentItemWithPlayerItem(
                    item = AVPlayerItem(uRL = url)
                )
                play()
            }
        }

        onDispose {
            PlayerViewLogger.logger.e { "testing Video onDispose" }
            avPlayerViewController.player?.pause()
        }
    }
}
