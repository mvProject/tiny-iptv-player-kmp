/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 24.10.23, 14:56
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun NoPlaybackView(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    @DrawableRes iconRes: Int
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // todo fix icon
        /*        Icon(
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
                )*/

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

        Text(
            // text = stringResource(id = textRes),
            text = "NoPlaybackView",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    //}
}

// todo replace preview
/*@Composable
@Preview(showBackground = true)
fun DarkPreviewNoPlaybackView() {
    VideoAppTheme(darkTheme = true) {
        NoPlaybackView(
            textRes = R.string.msg_no_internet_found,
            iconRes = R.drawable.ic_no_network
        )
    }
}*/


