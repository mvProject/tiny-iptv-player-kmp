package com.mvproject.tinyiptvkmp.features.channels.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.foundation.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.components.channels.ChannelPrograms as DesignSystemChannelPrograms

@Composable
fun ChannelPrograms(
    modifier: Modifier = Modifier,
    title: String = String.empty,
    programs: List<EpgProgram>,
) {
    DesignSystemChannelPrograms(
        modifier = modifier,
        title = title,
        programs = programs.map { program -> program.toChannelProgramUiModel() },
    )
}
