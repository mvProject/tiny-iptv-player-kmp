/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 31.01.24, 09:07
 *
 */

package com.mvproject.tinyiptvkmp.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended

@Composable
fun NoItemsView(
    modifier: Modifier = Modifier,
    title: String = String.empty,
    navigateTitle: String = String.empty,
    onNavigateClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(96.dp),
                imageVector = Icons.Filled.Info,
                tint = MaterialTheme.colorSchemeExtended.emptyProgramTitle,
                contentDescription = title,
            )

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            bottom = 24.dp,
                        ),
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorSchemeExtended.emptyProgramTitle,
                textAlign = TextAlign.Center,
            )

            if (navigateTitle.isNotEmpty()) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateClick)
                            .padding(
                                top = 16.dp,
                                bottom = 24.dp,
                            ),
                    text = navigateTitle,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewNoItemsView() {
    VideoAppTheme(darkTheme = true) {
        NoItemsView(navigateTitle = stringResource(id = R.string.app_name))
    }
}*/
