/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.presentation

import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.utils.actualDate
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ObserveChannelsSettingsUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.UpdateChannelsViewTypeUseCase
import com.mvproject.tinyiptvkmp.features.channels.presentation.GroupChannelsState.GroupChannelsOSD
import com.mvproject.tinyiptvkmp.features.channels.presentation.nav.GroupChannelsNavigator
import com.mvproject.tinyiptvkmp.features.epg.api.domain.model.TvChannelWithPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapProgramIds
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.mapPrograms
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.replaceUpdated
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.toggleFavorite
import com.mvproject.tinyiptvkmp.features.epg.api.domain.utils.withPrograms
import com.mvproject.tinyiptvkmp.features.groups.api.domain.model.ChannelGroupSelection
import com.mvproject.tinyiptvkmp.features.groups.api.domain.usecase.GetGroupChannelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    @InjectedParam args: GroupChannelsArgs,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val observeChannelsSettings: ObserveChannelsSettingsUseCase,
    private val updateChannelsViewType: UpdateChannelsViewTypeUseCase,
) : MviViewModel<GroupChannelsState, GroupChannelsAction, GroupChannelsEffect>() {

    private val group = args.group
    private val type = args.groupType
    private val selection = ChannelGroupSelection.fromRoute(
        group = args.group,
        groupType = args.groupType,
    )
    private val playlistId = args.playlistId

    private var lastRefresh: Long = 0

    private val navigator: GroupChannelsNavigator by inject()


    // todo refresh after return from playback

    override fun createStore() = createStore(
        initialState = GroupChannelsState(),
        invokeOnStart = { loadGroupChannels() },
    )

    override fun onIntent(intent: GroupChannelsAction) {
        when (intent) {
            GroupChannelsAction.CloseOsd -> closeOsd()
            GroupChannelsAction.NavigateBack -> launch { navigator.navigateUp() }
            is GroupChannelsAction.OpenOsd -> launch { openOsd(type = intent.type) }
            is GroupChannelsAction.SearchTextChange -> searchTextChange(text = intent.text)
            is GroupChannelsAction.SelectChannel -> launch {
                navigator.navigateToPlayer(
                    playlistId = playlistId,
                    name = intent.name,
                    group = intent.group,
                    groupType = type
                )
            }

            is GroupChannelsAction.ToggleFavorite -> launch {
                toggleFavorites(
                    channel = intent.channel,
                    type = intent.type
                )
            }

            is GroupChannelsAction.ViewTypeChange -> launch { viewTypeChange(type = intent.type) }
        }
    }

    private suspend fun loadGroupChannels() {
        val viewType = observeChannelsSettings().first().channelsViewType
        val groupChannels = getGroupChannelsUseCase(
            playlistId = playlistId,
            selection = selection,
        )
        setState {
            copy(
                viewType = viewType,
                currentGroup = group,
                channels = groupChannels.withPrograms()
            )
        }
    }

    fun loadChannelsByGroups() {
        // TODO(performance): Skip resume EPG refresh when channels are empty or a refresh is already running.
        // TODO: Serialize EPG refreshes with a refresh Job/Mutex or update the refresh gate before fetching.
        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgPrograms()
        }
    }

    private fun searchTextChange(text: String) {
        setState { copy(searchString = text) }
    }

    private fun closeOsd() {
        setState { copy(osdType = null) }
    }

    private suspend fun openOsd(type: GroupChannelsOSD) {
        // TODO: Combine selected channel/programs and osdType into one state update after programs are loaded.
        if (type is GroupChannelsOSD.ChannelPrograms) {
            toggleProgramVisibility(
                name = type.channel.channelName,
                programId = type.channel.programId
            )
        }
        setState { copy(osdType = type) }
    }

    private suspend fun refreshEpgPrograms() {
        if (actualDate - lastRefresh < 1.minutes.inWholeMilliseconds) {
            return
        }
        val channels = state.value.channels
        val channelsIds = channels.mapProgramIds()

        if (channelsIds.isNotEmpty()) {
            val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
            if (channelsEpgData.entries.isNotEmpty()) {
                val channelsWithPrograms = channels.mapPrograms(channelEpgMap = channelsEpgData)

                setState { copy(channels = channelsWithPrograms) }
            }

            lastRefresh = actualDate
        }
    }

    private suspend fun toggleProgramVisibility(name: String, programId: String) {
        val programs = if (programId.isNotBlank()) {
            getChannelsEpgUseCase(channelId = programId)
        } else {
            emptyList()
        }

        setState {
            copy(
                selectedName = name,
                selectedPrograms = programs
            )
        }
    }

    private suspend fun viewTypeChange(type: ChannelsViewType) {
        if (state.value.viewType != type) {
            updateChannelsViewType(type)
            setState { copy(viewType = type) }
        }
    }

    private suspend fun toggleFavorites(
        channel: TvChannelWithPrograms,
        type: FavoriteType,
    ) {
        val updatedChannel = channel.toggleFavorite(type = type)

        val updatedChannels = state.value.channels
            .replaceUpdated(channel = updatedChannel)

        setState {
            copy(channels = updatedChannels, osdType = null)
        }

        toggleFavoriteChannelUseCase(
            playlistId = playlistId,
            channel = channel.channel,
            type = type,
        )
    }
}
