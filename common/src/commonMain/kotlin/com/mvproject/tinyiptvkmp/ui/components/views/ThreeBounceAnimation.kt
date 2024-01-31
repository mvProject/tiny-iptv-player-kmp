/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 31.01.24, 09:20
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import kotlinx.coroutines.delay

@Composable
fun ThreeBounceAnimation(
    circleSize: Dp = MaterialTheme.dimens.size24,
    circleColor: Color = MaterialTheme.colorScheme.onSurface
) {

    val dots = listOf(
        remember { Animatable(0.2f) },
        remember { Animatable(0.2f) },
        remember { Animatable(0.2f) },
    )

    dots.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 200L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        600,
                        easing = FastOutLinearInEasing
                    ),
                    repeatMode = RepeatMode.Reverse,
                )
            )
        }
    }

    val dys = dots.map { it.value }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        dys.forEachIndexed { index, dy ->
            Box(
                Modifier
                    .size(circleSize)
                    .scale(dy)
                    .alpha(dy)
                    .drawBehind {
                        drawCircle(circleColor)
                    }
            )
        }
    }
}