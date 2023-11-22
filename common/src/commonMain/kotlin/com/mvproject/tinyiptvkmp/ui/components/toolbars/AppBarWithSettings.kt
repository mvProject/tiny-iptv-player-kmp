/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 26.10.23, 13:23
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.toolbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.mvproject.tinyiptvkmp.MainRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithSettings(
    onSettingsClicked: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = MainRes.string.app_name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(
                onClick = onSettingsClicked
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

// todo replace preview
/*
@Preview(showBackground = true)
@Composable
fun AppBarWithSettingsDarkPreview() {
    VideoAppTheme(darkTheme = true) {
        AppBarWithSettings()
    }
}*/
