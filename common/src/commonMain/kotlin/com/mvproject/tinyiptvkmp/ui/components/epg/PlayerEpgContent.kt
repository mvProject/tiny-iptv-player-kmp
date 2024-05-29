/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.ui.screens.channels.data.TvPlaylistChannelEpg
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import org.jetbrains.compose.resources.stringResource
import tinyiptvkmp.common.generated.resources.Res
import tinyiptvkmp.common.generated.resources.msg_no_epg_found

@Composable
fun PlayerEpgContent(
    modifier: Modifier = Modifier,
    epgList: TvPlaylistChannelEpg,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (epgList.items.isEmpty()) {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.size12)
                        .align(Alignment.Center),
                text = stringResource(Res.string.msg_no_epg_found),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size10),
            contentPadding =
                PaddingValues(
                    vertical = MaterialTheme.dimens.size4,
                    horizontal = MaterialTheme.dimens.size2,
                ),
            content = {
                items(
                    items = epgList.items,
                    key = { epg -> epg.key },
                ) { epg ->
                    PlayerEpgItem(
                        modifier =
                            Modifier
                                .padding(start = MaterialTheme.dimens.size4),
                        program = epg,
                    )
                }
            },
        )
    }
}

// todo replace preview
/*
@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerOverlayEpgView() {
    VideoAppTheme(darkTheme = true) {
        PlayerEpgContent(epgList = PreviewTestData.testEpgPrograms)
    }
}*/
