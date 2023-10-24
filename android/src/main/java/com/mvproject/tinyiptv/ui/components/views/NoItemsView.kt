/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.10.23, 18:13
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING

@Composable
fun NoItemsView(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.msg_no_items_found),
    navigateTitle: String = EMPTY_STRING,
    onNavigateClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(MaterialTheme.dimens.size96),
                imageVector = Icons.Filled.Info,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = title
            )

            Text(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.dimens.size16,
                        bottom = MaterialTheme.dimens.size24
                    ),
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (navigateTitle.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.dimens.size16,
                            bottom = MaterialTheme.dimens.size24
                        )
                        .clickable { onNavigateClick() },
                    text = navigateTitle,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewNoItemsView() {
    VideoAppTheme() {
        NoItemsView(navigateTitle = stringResource(id = R.string.app_name))
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewNoItemsView() {
    VideoAppTheme(darkTheme = true) {
        NoItemsView(navigateTitle = stringResource(id = R.string.app_name))
    }
}