/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.components.toolbars

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mvproject.tinyiptvkmp.core.foundation.model.ChannelsViewType

@Composable
fun AppBarWithSearch(
    appBarTitle: String,
    searchTextState: String,
    searchPlaceholderText: String,
    onBackClick: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
    onViewTypeChange: (ChannelsViewType) -> Unit = {}
) {
    var isSearching by remember {
        mutableStateOf(false)
    }
    AnimatedContent(
        targetState = isSearching,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it }
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it }
            )
        },
        label = "AppBarWithSearch"
    ) { state ->
        if (state) {
            SearchAppBar(
                text = searchTextState,
                placeholderText = searchPlaceholderText,
                onTextChange = onTextChange,
                onCloseClicked = {
                    isSearching = false
                }
            )
        } else {
            AppBarWithActions(
                appBarTitle = appBarTitle,
                onBackClick = onBackClick,
                onSearchClicked = {
                    isSearching = true
                },
                onViewTypeChange = onViewTypeChange
            )
        }
    }
}

