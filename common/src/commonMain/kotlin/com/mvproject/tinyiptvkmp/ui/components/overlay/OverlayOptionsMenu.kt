/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 29.11.23, 16:08
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants

@Composable
fun OverlayOptionsMenu(
    modifier: Modifier = Modifier,
    title: String? = null,
    items: List<String>,
    selectedIndex: Int = AppConstants.INT_NO_VALUE,
    onItemSelected: (index: Int) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .width(MaterialTheme.dimens.size310)
            .padding(MaterialTheme.dimens.size8),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = MaterialTheme.dimens.size8
    ) {
        val listState = rememberLazyListState()
        if (selectedIndex > AppConstants.INT_NO_VALUE) {
            LaunchedEffect(selectedIndex) {
                listState.scrollToItem(index = selectedIndex)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState
        ) {
            title?.let { text ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            .padding(MaterialTheme.dimens.size12)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            itemsIndexed(items) { index, item ->
                val selectedItem = index == selectedIndex
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(),
                    onClick = { onItemSelected(index) }
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (selectedItem)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface,

                        )
                }

                if (index < items.lastIndex) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.dimens.size16),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}