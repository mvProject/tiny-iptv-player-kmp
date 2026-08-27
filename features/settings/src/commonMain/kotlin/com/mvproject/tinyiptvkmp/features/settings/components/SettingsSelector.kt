package com.mvproject.tinyiptvkmp.features.settings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.core.foundation.common.INT_NO_VALUE
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize
import com.mvproject.tinyiptvkmp.core.theme.dimensionText

@Composable
fun SettingsSelector(
    title: String,
    options: List<String> = emptyList(),
    selectedIndex: Int = INT_NO_VALUE,
    isExpanded: Boolean = false,
    onClick: () -> Unit,
    onSelect: (Int) -> Unit,
) {
    val borderColor = if (isExpanded)
        MaterialTheme.colorSchemeExtended.activeBorder
    else
        MaterialTheme.colorSchemeExtended.inactiveBorder

    Column(
        modifier = Modifier.fillMaxWidth().border(
            width = MaterialTheme.dimensionSize.size1,
            color = borderColor,
            shape = MaterialTheme.shapes.extraSmall
        )
    ) {
        ListItem(
            modifier = Modifier.clickable(onClick = onClick),
            overlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.dimensionText.font10,
                )
            },
            headlineContent = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = options[selectedIndex],
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            trailingContent = {
                val trailingIcon = if (isExpanded)
                    Icons.Default.ArrowDropUp
                else
                    Icons.Default.ArrowDropDown

                Icon(
                    imageVector = trailingIcon,
                    contentDescription = trailingIcon.name
                )

            }
        )

        AnimatedContent(
            targetState = isExpanded,
            transitionSpec = ({
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) togetherWith slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up
                )
            })
        ) { isOpen ->

            if (isOpen) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(options) { index, item ->
                        val isSelected = index == selectedIndex

                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(),
                            onClick = {
                                //  isSelectPlaylistOpen = false
                                onSelect(index)
                            },
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.titleSmall,
                                color =
                                    if (isSelected) {
                                        MaterialTheme.colorSchemeExtended.activeInput
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                            )
                        }

                        if (index < options.lastIndex) {
                            HorizontalDivider(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimensionSize.size16),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}
