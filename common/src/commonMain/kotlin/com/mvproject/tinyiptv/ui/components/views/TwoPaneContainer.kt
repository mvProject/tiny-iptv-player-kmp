/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 30.10.23, 14:21
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TwoPaneContainer(
    isFullScreen: Boolean,
    isCompact: Boolean,
    mainContent: @Composable () -> Unit,
    addonContent: @Composable () -> Unit,
) {
    if (isFullScreen) {
        mainContent()
    } else {
        if (isCompact) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    mainContent()
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    addonContent()
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxHeight()
                ) {
                    mainContent()
                }
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                ) {
                    addonContent()
                }
            }
        }
    }
}