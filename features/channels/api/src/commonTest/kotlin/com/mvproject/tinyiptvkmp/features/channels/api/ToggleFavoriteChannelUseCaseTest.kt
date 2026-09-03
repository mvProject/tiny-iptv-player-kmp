package com.mvproject.tinyiptvkmp.features.channels.api

import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.FavoriteType
import com.mvproject.tinyiptvkmp.features.channels.api.domain.model.TvChannel
import com.mvproject.tinyiptvkmp.features.channels.api.domain.repository.ChannelFavoriteRepository
import com.mvproject.tinyiptvkmp.features.channels.api.domain.usecase.ToggleFavoriteChannelUseCaseImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ToggleFavoriteChannelUseCaseTest {
    @Test
    fun addsChannelWhenItIsNotFavorite() = runTest {
        val repository = RecordingFavoriteRepository()
        val useCase = ToggleFavoriteChannelUseCaseImpl(repository)

        useCase(
            playlistId = "playlist",
            channel = channel(favoriteType = FavoriteType.NONE),
            type = FavoriteType.MOVIE,
        )

        assertEquals(listOf("add:playlist:News:url-news:MOVIE"), repository.operations)
    }

    @Test
    fun updatesTypeWhenChannelAlreadyHasDifferentFavoriteType() = runTest {
        val repository = RecordingFavoriteRepository()
        val useCase = ToggleFavoriteChannelUseCaseImpl(repository)

        useCase(
            playlistId = "playlist",
            channel = channel(favoriteType = FavoriteType.MOVIE),
            type = FavoriteType.SPORT,
        )

        assertEquals(listOf("update:playlist:url-news:SPORT"), repository.operations)
    }

    @Test
    fun removesChannelWhenSameTypeIsSelectedAgain() = runTest {
        val repository = RecordingFavoriteRepository()
        val useCase = ToggleFavoriteChannelUseCaseImpl(repository)

        useCase(
            playlistId = "playlist",
            channel = channel(favoriteType = FavoriteType.MOVIE),
            type = FavoriteType.MOVIE,
        )

        assertEquals(listOf("delete:playlist:url-news"), repository.operations)
    }

    @Test
    fun removesChannelWhenNoneIsSelected() = runTest {
        val repository = RecordingFavoriteRepository()
        val useCase = ToggleFavoriteChannelUseCaseImpl(repository)

        useCase(
            playlistId = "playlist",
            channel = channel(favoriteType = FavoriteType.SPORT),
            type = FavoriteType.NONE,
        )

        assertEquals(listOf("delete:playlist:url-news"), repository.operations)
    }

    @Test
    fun doesNothingWhenNoneIsSelectedForChannelThatIsNotFavorite() = runTest {
        val repository = RecordingFavoriteRepository()
        val useCase = ToggleFavoriteChannelUseCaseImpl(repository)

        useCase(
            playlistId = "playlist",
            channel = channel(favoriteType = FavoriteType.NONE),
            type = FavoriteType.NONE,
        )

        assertEquals(emptyList(), repository.operations)
    }

    private fun channel(favoriteType: FavoriteType) =
        TvChannel(
            channelName = "News",
            channelUrl = "url-news",
            favoriteType = favoriteType,
        )
}

private class RecordingFavoriteRepository : ChannelFavoriteRepository {
    val operations = mutableListOf<String>()

    override suspend fun addChannelToFavorite(
        playlistId: String,
        channelName: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) {
        operations += "add:$playlistId:$channelName:$channelUrl:${favoriteType.name}"
    }

    override suspend fun deleteChannelFromFavorite(playlistId: String, channelUrl: String) {
        operations += "delete:$playlistId:$channelUrl"
    }

    override suspend fun updateFavoriteType(
        playlistId: String,
        channelUrl: String,
        favoriteType: FavoriteType,
    ) {
        operations += "update:$playlistId:$channelUrl:${favoriteType.name}"
    }

    override suspend fun loadSelectedFavoriteChannels(playlistId: String): List<FavoriteChannel> =
        emptyList()

    override suspend fun loadFavoriteChannelUrls(): List<String> = emptyList()

    override suspend fun loadFavoriteChannelUrls(playlistId: String): List<String> = emptyList()

    override suspend fun loadFavoriteChannelNamesByUrl(playlistId: String): Map<String, String> =
        emptyMap()

    override suspend fun updateFavoriteChannel(
        playlistId: String,
        channelName: String,
        channelUrl: String,
    ) = Unit

    override suspend fun updateFavoriteChannels(
        playlistId: String,
        channelNamesByUrl: Map<String, String>,
    ) = Unit

    override suspend fun deletePlaylistFavoriteChannels(playlistId: String) = Unit
}
