/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 23.02.24, 11:07
 *
 */

package com.mvproject.tinyiptvkmp.core.components.toolbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.components.buttons.MenuButton
import com.mvproject.tinyiptvkmp.core.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBackNav(
    appBarTitle: String,
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            MenuButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBackClick
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarWithBackNavPreview() {
    AppTheme {
        AppBarWithBackNav(
            appBarTitle = "Settings"
        )
    }
}
