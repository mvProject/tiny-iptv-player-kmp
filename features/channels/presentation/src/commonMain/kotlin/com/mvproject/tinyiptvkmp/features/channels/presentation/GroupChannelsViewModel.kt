/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels.presentation

import com.mvproject.tinyiptvkmp.core.base.mvi.MviViewModel
import com.mvproject.tinyiptvkmp.core.base.mvi.runCatchingSuspend
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.foundation.utils.actualDate
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
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
import com.mvproject.tinyiptvkmp.features.player.api.domain.coordinator.PlaybackSourceCoordinator
import com.mvproject.tinyiptvkmp.features.player.api.domain.model.PlaybackSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    private val playbackSourceCoordinator: PlaybackSourceCoordinator,
) : MviViewModel<GroupChannelsState, GroupChannelsAction, GroupChannelsEffect>() {

    private val group = args.group
    private val type = args.groupType
    private val selection = ChannelGroupSelection.fromRoute(
        group = args.group,
        groupType = args.groupType,
    )
    private val playlistId = args.playlistId

    private var lastRefresh: Long = 0
    private var skipInitialSearchLoad = false

    private val navigator: GroupChannelsNavigator by inject()

    override fun createStore() = createStore(
        initialState = GroupChannelsState(),
        started = SharingStarted.Eagerly,
        invokeOnStart = {
            restoreInitialPlaybackSource()
            coroutineScope {
                launch { observeSearchQueries() }
                launch { observePlaybackSourceUpdates() }
            }
        },
    )

    override fun onIntent(intent: GroupChannelsAction) {
        when (intent) {
            GroupChannelsAction.CloseOsd -> closeOsd()
            GroupChannelsAction.LoadMore -> requestNextPage()
            GroupChannelsAction.NavigateBack -> launch { navigator.navigateUp() }
            is GroupChannelsAction.OpenOsd -> launch { openOsd(type = intent.type) }
            GroupChannelsAction.ScrollRequestConsumed -> setState {
                copy(scrollToChannelId = null)
            }
            is GroupChannelsAction.SearchTextChange -> searchTextChange(text = intent.text)
            is GroupChannelsAction.SelectChannel -> launch {
                publishPlaybackSource(channel = intent.channel)
                navigator.navigateToPlayer(
                    playlistId = playlistId,
                    name = intent.channel.channelName,
                    url = intent.channel.channelUrl,
                    group = group,
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

    private suspend fun observeSearchQueries() {
        var isFirstEmission = true
        state
            .map { state -> state.searchString }
            .distinctUntilChanged()
            .collectLatest { searchQuery ->
                if (isFirstEmission && skipInitialSearchLoad && searchQuery.isBlank()) {
                    isFirstEmission = false
                    return@collectLatest
                }
                isFirstEmission = false
                loadFirstPage(searchQuery = searchQuery)
            }
    }

    private suspend fun observePlaybackSourceUpdates() {
        playbackSourceCoordinator.updates.collect { source ->
            if (source.matchesCurrentRoute()) {
                restorePlaybackSource(source = source)
            }
        }
    }

    private suspend fun restoreInitialPlaybackSource() {
        val source = playbackSourceCoordinator.source.value
        if (source != null && source.matchesCurrentRoute()) {
            restorePlaybackSource(source = source)
            skipInitialSearchLoad = true
        }
    }

    private suspend fun loadFirstPage(searchQuery: String) {
        val viewType = observeChannelsSettings().first().channelsViewType
        setState {
            copy(
                isLoading = true,
                isLoadingMore = false,
                currentGroup = group,
                viewType = viewType,
                nextChannelsOffset = 0,
                hasMoreChannels = false,
            )
        }

        runCatchingSuspend {
            getGroupChannelsUseCase(
                playlistId = playlistId,
                selection = selection,
                offset = 0,
                searchQuery = searchQuery,
            )
        }.onSuccess { channelsPage ->
            val channels = channelsPage.channels.withPrograms()
            setState {
                if (this.searchString == searchQuery) {
                    copy(
                        isLoading = false,
                        viewType = viewType,
                        currentGroup = group,
                        channels = channels,
                        nextChannelsOffset = channelsPage.nextOffset,
                        hasMoreChannels = channelsPage.hasMore,
                    )
                } else {
                    this
                }
            }
            if (searchQuery.isBlank()) {
                publishLoadedChannels(
                    channels = channels,
                    nextOffset = channelsPage.nextOffset,
                    hasMore = channelsPage.hasMore,
                    replace = true,
                )
            }
        }.onFailure { throwable ->
            logger.e(throwable) { "Failed to load group channels" }
            setState {
                if (this.searchString == searchQuery) {
                    copy(isLoading = false, isLoadingMore = false)
                } else {
                    this
                }
            }
        }
    }

    fun loadChannelsByGroups() {
        launch { refreshEpgPrograms() }
    }

    private fun searchTextChange(text: String) {
        setState { copy(searchString = text) }
    }

    private fun requestNextPage() {
        val currentState = getState()
        if (
            currentState.isLoading ||
            currentState.isLoadingMore ||
            !currentState.hasMoreChannels
        ) {
            return
        }

        val offset = currentState.nextChannelsOffset
        val searchQuery = currentState.searchString
        setState { copy(isLoadingMore = true) }
        launch { loadNextPage(offset = offset, searchQuery = searchQuery) }
    }

    private suspend fun loadNextPage(offset: Int, searchQuery: String) {
        runCatchingSuspend {
            getGroupChannelsUseCase(
                playlistId = playlistId,
                selection = selection,
                offset = offset,
                searchQuery = searchQuery,
            )
        }.onSuccess { channelsPage ->
            val channels = channelsPage.channels.withPrograms()
            setState {
                if (this.searchString == searchQuery && this.nextChannelsOffset == offset) {
                    copy(
                        isLoadingMore = false,
                        channels = this.channels + channels,
                        nextChannelsOffset = channelsPage.nextOffset,
                        hasMoreChannels = channelsPage.hasMore,
                    )
                } else {
                    this
                }
            }
            if (searchQuery.isBlank()) {
                publishLoadedChannels(
                    channels = channels,
                    nextOffset = channelsPage.nextOffset,
                    hasMore = channelsPage.hasMore,
                    replace = false,
                )
            }
        }.onFailure { throwable ->
            logger.e(throwable) { "Failed to load more group channels" }
            setState {
                if (this.searchString == searchQuery && this.nextChannelsOffset == offset) {
                    copy(isLoadingMore = false)
                } else {
                    this
                }
            }
        }
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

    private fun publishPlaybackSource(channel: TvChannelWithPrograms) {
        val currentState = state.value
        val selectedIndex = currentState.channels.indexOfFirst {
            it.channelUrl == channel.channelUrl
        }
        playbackSourceCoordinator.setSource(
            PlaybackSource(
                playlistId = playlistId,
                group = group,
                groupType = type,
                selection = selection,
                selectedChannel = channel.channel,
                selectedIndex = selectedIndex,
                loadedChannels = currentState.channels.map { it.channel },
                nextChannelsOffset = currentState.nextChannelsOffset,
                hasMoreChannels = currentState.hasMoreChannels,
            )
        )
    }

    private fun publishLoadedChannels(
        channels: List<TvChannelWithPrograms>,
        nextOffset: Int,
        hasMore: Boolean,
        replace: Boolean,
    ) {
        val source = playbackSourceCoordinator.source.value
        if (source == null || !source.matchesCurrentRoute()) return

        if (replace) {
            val selectedIndex = channels.indexOfFirst {
                it.channelUrl == source.selectedChannel.channelUrl
            }
            if (selectedIndex < 0) return
            playbackSourceCoordinator.replaceLoadedChannels(
                channels = channels.map { it.channel },
                selectedIndex = selectedIndex,
                nextOffset = nextOffset,
                hasMore = hasMore,
            )
        } else {
            playbackSourceCoordinator.appendLoadedChannels(
                channels = channels.map { it.channel },
                nextOffset = nextOffset,
                hasMore = hasMore,
            )
        }
    }

    private fun restorePlaybackSource(source: PlaybackSource) {
        val channels = source.loadedChannels.withPrograms()
        val targetChannelId = source.selectedChannel.channelUiId
        setState {
            if (
                currentGroup == source.group &&
                channels.map { channel -> channel.channelUrl } ==
                this.channels.map { channel -> channel.channelUrl } &&
                nextChannelsOffset == source.nextChannelsOffset &&
                hasMoreChannels == source.hasMoreChannels &&
                scrollToChannelId == targetChannelId
            ) {
                return@setState this
            }

            copy(
                currentGroup = source.group,
                channels = channels,
                nextChannelsOffset = source.nextChannelsOffset,
                hasMoreChannels = source.hasMoreChannels,
                scrollToChannelId = targetChannelId,
            )
        }
    }

    private fun PlaybackSource.matchesCurrentRoute(): Boolean =
        playlistId == this@GroupChannelsViewModel.playlistId &&
                group == this@GroupChannelsViewModel.group &&
                groupType == this@GroupChannelsViewModel.type

    private val TvChannel.channelUiId: String
        get() = "$channelName$channelUrl"
}
