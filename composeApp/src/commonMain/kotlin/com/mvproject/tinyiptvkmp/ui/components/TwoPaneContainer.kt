package com.mvproject.tinyiptvkmp.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun TwoPaneContainer(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
)