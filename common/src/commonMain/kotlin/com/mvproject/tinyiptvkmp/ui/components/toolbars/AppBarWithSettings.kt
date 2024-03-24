/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.app_name

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun AppBarWithSettings(onSettingsClicked: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        actions = {
            IconButton(
                onClick = onSettingsClicked,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
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
