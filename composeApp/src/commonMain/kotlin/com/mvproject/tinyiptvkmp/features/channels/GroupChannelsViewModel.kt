/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 08.08.24, 20:26
 *
 */

package com.mvproject.tinyiptvkmp.features.channels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.mvproject.tinyiptvkmp.core.common.mvi.MviCore
import com.mvproject.tinyiptvkmp.core.common.mvi.mviCore
import com.mvproject.tinyiptvkmp.core.common.utils.CommonUtils.empty
import com.mvproject.tinyiptvkmp.core.common.utils.TimeUtils
import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.ChannelsViewType.Companion.mapViewType
import com.mvproject.tinyiptvkmp.core.domain.enums.FavoriteType
import com.mvproject.tinyiptvkmp.core.domain.model.EpgProgram
import com.mvproject.tinyiptvkmp.core.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsEpgUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.GetGroupChannelsUseCase
import com.mvproject.tinyiptvkmp.core.domain.usecase.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.mapProgramIds
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.mapPrograms
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.replaceUpdated
import com.mvproject.tinyiptvkmp.core.domain.utils.ChannelsUtils.toggleFavorite
import com.mvproject.tinyiptvkmp.navigation.AppRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class GroupChannelsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getChannelsEpgUseCase: GetChannelsEpgUseCase,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val getGroupChannelsEpgUseCase: GetGroupChannelsEpgUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel(),
    MviCore<GroupChannelsUiState, GroupChannelsUiAction, GroupChannelsUiEffect> by mviCore(
        GroupChannelsUiState()
    ) {

    private val args = savedStateHandle.toRoute<AppRoutes.TvPlaylistChannels>()

    private val group = args.group
    private val type = args.groupType

    private var lastRefresh: Long = 0

    init {
        viewModelScope.launch {
            val groupChannels = getGroupChannelsUseCase(group = group, groupType = type)
            val viewType = preferenceRepository.getChannelsViewType().mapViewType()

            updateUiState {
                copy(
                    viewType = viewType,
                    currentGroup = group,
                    channels = groupChannels
                )
            }
        }
    }

    override fun onAction(uiAction: GroupChannelsUiAction) {
        when (uiAction) {
            GroupChannelsUiAction.NavigateBack -> viewModelScope.postUiEffect(GroupChannelsUiEffect.OnNavigateBack)
            is GroupChannelsUiAction.SelectChannel -> viewModelScope.postUiEffect(
                GroupChannelsUiEffect.OnNavigateToPlayer(
                    name = uiAction.name,
                    group = uiAction.group,
                    groupType = type
                )
            )

            is GroupChannelsUiAction.SearchTextChange -> searchTextChange(text = uiAction.text)
            is GroupChannelsUiAction.ToggleEpgVisibility -> toggleEpgVisibility(
                name = uiAction.name,
                epgId = uiAction.programId
            )

            is GroupChannelsUiAction.ToggleFavorite -> toggleFavorites(
                channel = uiAction.channel,
                type = uiAction.type
            )

            is GroupChannelsUiAction.ViewTypeChange -> viewTypeChange(type = uiAction.type)
        }
    }

    fun loadChannelsByGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshEpgPrograms()
        }
    }

    private suspend fun refreshEpgPrograms() {
        if (TimeUtils.actualDate - lastRefresh > 1.minutes.inWholeMilliseconds) {
            val channels = uiState.value.channels
            val channelsIds = channels.mapProgramIds()
            if (channelsIds.isNotEmpty()) {
                val channelsEpgData = getGroupChannelsEpgUseCase(channelsIds = channelsIds)
                val channelsWithPrograms = channels.mapPrograms(channelEpgMap = channelsEpgData)

                updateUiState {
                    copy(channels = channelsWithPrograms)
                }

                lastRefresh = TimeUtils.actualDate
            }
        }
    }

    private fun searchTextChange(text: String) {
        updateUiState {
            copy(searchString = text)
        }
    }

    private fun toggleEpgVisibility(name: String, epgId: String) {
        Logger.w("testing name:$name,epgId:$epgId")
        viewModelScope.launch(Dispatchers.IO) {
            val programs = if (epgId.isNotBlank()) {
                getChannelsEpgUseCase(channelId = epgId)
            } else {
                emptyList()
            }

            updateUiState {
                copy(
                    selectedName = name,
                    selectedPrograms = programs
                )
            }
        }
    }

    private fun viewTypeChange(type: ChannelsViewType) {
        if (uiState.value.viewType != type) {
            viewModelScope.launch {
                preferenceRepository.setChannelsViewType(type = type.name)
                updateUiState {
                    copy(viewType = type)
                }
            }
        }
    }

    private fun toggleFavorites(
        channel: TvChannel,
        type: FavoriteType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Logger.w("testing toggleFavorites type $type")

            val updatedChannel = channel.toggleFavorite(type = type)

            val updatedChannels = uiState.value.channels
                .replaceUpdated(channel = updatedChannel)

            updateUiState {
                copy(channels = updatedChannels)
            }

            toggleFavoriteChannelUseCase(channel = channel)
        }
    }
}

@Immutable
data class GroupChannelsUiState(
    val currentGroup: String = String.empty,
    val isLoading: Boolean = false,
    val isEpgVisible: Boolean = false,
    val searchString: String = String.empty,
    val viewType: ChannelsViewType = ChannelsViewType.LIST,
    val channels: List<TvChannel> = emptyList(),
    val selectedName: String = String.empty,
    val selectedPrograms: List<EpgProgram> = emptyList()
)

sealed interface GroupChannelsUiAction {
    data class ToggleFavorite(val channel: TvChannel, val type: FavoriteType) :
        GroupChannelsUiAction

    data class SearchTextChange(val text: String) : GroupChannelsUiAction
    data class ViewTypeChange(val type: ChannelsViewType) : GroupChannelsUiAction
    data class ToggleEpgVisibility(
        val name: String = String.empty,
        val programId: String = String.empty
    ) : GroupChannelsUiAction

    data class SelectChannel(val name: String, val group: String) : GroupChannelsUiAction
    data object NavigateBack : GroupChannelsUiAction
}

sealed interface GroupChannelsUiEffect {
    data class OnNavigateToPlayer(val name: String, val group: String, val groupType: String) :
        GroupChannelsUiEffect

    data object OnNavigateBack : GroupChannelsUiEffect
}
