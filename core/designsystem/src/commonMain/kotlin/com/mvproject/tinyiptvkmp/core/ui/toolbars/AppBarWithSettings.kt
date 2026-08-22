/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.core.ui.toolbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.ui.buttons.MenuButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithSettings(
    appBarTitle: String,
    onSettingsClicked: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        actions = {
            MenuButton(
                imageVector = Icons.Default.Settings,
                onClick = onSettingsClicked
            )
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarWithSettingsPreview() {
    AppTheme {
        AppBarWithSettings(appBarTitle = "Tiny Iptv Player")
    }
}
