/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 18.05.23, 15:41
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun NoPlaybackView(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    @DrawableRes iconRes: Int
) {
    /*    Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {*/
        Column(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(MaterialTheme.dimens.size96)
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    .padding(MaterialTheme.dimens.size22),
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(id = textRes),
                tint = MaterialTheme.colorScheme.primaryContainer
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

            Text(
                text = stringResource(id = textRes),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    //}
}

@Composable
@Preview(showBackground = true)
fun PreviewNoPlaybackView() {
    VideoAppTheme() {
        NoPlaybackView(
            textRes = R.string.msg_no_internet_found,
            iconRes = R.drawable.ic_no_network
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewNoPlaybackView() {
    VideoAppTheme(darkTheme = true) {
        NoPlaybackView(
            textRes = R.string.msg_no_internet_found,
            iconRes = R.drawable.ic_no_network
        )
    }
}


