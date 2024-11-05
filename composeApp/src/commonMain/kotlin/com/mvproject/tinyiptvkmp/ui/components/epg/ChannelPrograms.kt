/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.05.24, 13:51
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.epg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.data.PreviewTestData
import com.mvproject.tinyiptvkmp.data.model.epg.EpgProgram
import com.mvproject.tinyiptvkmp.ui.components.modifiers.roundedHeader
import com.mvproject.tinyiptvkmp.ui.theme.VideoAppTheme
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tinyiptvkmp.composeapp.generated.resources.Res
import tinyiptvkmp.composeapp.generated.resources.msg_no_epg_found

@Composable
internal fun ChannelPrograms(
    modifier: Modifier = Modifier,
    title: String = String.empty,
    programs: List<EpgProgram>,
) {
    Column(
        modifier = modifier
        //  .clip(MaterialTheme.shapes.small)
    ) {
        if (title.isNotBlank()) {
            ChannelProgramsTitle(
                modifier = Modifier.roundedHeader(),
                title = title
            )
        }

        if (programs.isEmpty()) {
            ChannelProgramsEmpty(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(Res.string.msg_no_epg_found)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2)
        ) {
            items(
                items = programs,
                key = { program -> program.key },
            ) { program ->
                ChannelProgramItem(program = program)
            }
        }
    }
}

@Composable
@Preview
private fun ChannelProgramsPreview() {
    VideoAppTheme() {
        ChannelPrograms(programs = PreviewTestData.testEpgPrograms)
    }
}
