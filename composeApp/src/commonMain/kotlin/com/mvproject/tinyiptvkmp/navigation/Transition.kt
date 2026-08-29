package com.mvproject.tinyiptvkmp.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

private const val TIME_DURATION = 300

fun appTransitionSpec(): ContentTransform =
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(durationMillis = TIME_DURATION, easing = EaseOutQuad)
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(durationMillis = TIME_DURATION, easing = EaseOutQuad)
    )

fun appPopTransitionSpec(): ContentTransform =
    slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(durationMillis = TIME_DURATION, easing = EaseOutQuad)
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(durationMillis = TIME_DURATION, easing = EaseOutQuad)
    )