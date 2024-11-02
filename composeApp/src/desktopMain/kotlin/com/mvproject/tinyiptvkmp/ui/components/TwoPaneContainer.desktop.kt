package com.mvproject.tinyiptvkmp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.theme.dimens

@Composable
actual fun TwoPaneContainer(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
            Modifier
                .weight(MaterialTheme.dimens.weight6)
                .fillMaxHeight(),
        ) {
            first()
        }
        Column(
            modifier =
            Modifier
                .weight(MaterialTheme.dimens.weight2)
                .fillMaxHeight(),
        ) {
            second()
        }
    }
}