package com.mvproject.tinyiptvkmp.core.components.selectors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

/**
 * Collapsible single-select field.
 *
 * [options] are identified by the caller-supplied [selected] value instead of by index, so a
 * reordered or out-of-range list can never select or crash on the wrong option.
 *
 * @param expanded when `null` the field owns its expansion state, otherwise expansion belongs to
 * the caller and [onExpandedChange] only reports the requested state.
 */
@Composable
fun <T> OptionSelector(
    label: String,
    options: List<T>,
    optionLabel: (T) -> String,
    selected: T?,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: (Boolean) -> Unit = {},
    enabled: Boolean = true,
) {
    var localExpanded by remember { mutableStateOf(false) }
    val isExpanded = expanded ?: localExpanded

    val setExpanded: (Boolean) -> Unit = { value ->
        if (expanded == null) localExpanded = value
        onExpandedChange(value)
    }

    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = DISABLED_ALPHA)
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = MaterialTheme.dimensionSize.size1,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(enabled = enabled, role = Role.Button) { setExpanded(!isExpanded) },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                    headlineColor = contentColor,
                    overlineColor = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        contentColor.copy(alpha = DISABLED_ALPHA)
                    },
                    trailingIconColor = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        contentColor
                    },
                ),
                overlineContent = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                headlineContent = {
                    Text(
                        text = selected?.let(optionLabel).orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                trailingContent = {
                    Icon(
                        modifier = Modifier.rotate(if (isExpanded) EXPANDED_ROTATION else 0f),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                },
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    options.forEachIndexed { index, option ->
                        OptionRow(
                            label = optionLabel(option),
                            isSelected = option == selected,
                            onClick = {
                                setExpanded(false)
                                onSelected(option)
                            },
                        )

                        if (index < options.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    horizontal = MaterialTheme.dimensionSize.size16
                                ),
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Always-visible single-select list, for surfaces that cannot host an expanding field such as the
 * player and group overlays.
 */
@Composable
fun <T> OptionGroup(
    options: List<T>,
    optionLabel: (T) -> String,
    selected: T?,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        options.forEachIndexed { index, option ->
            OptionRow(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size4),
                label = optionLabel(option),
                isSelected = option == selected,
                onClick = { onSelected(option) },
            )

            if (index < options.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimensionSize.size12),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
    }
}

@Composable
private fun OptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        selected = isSelected,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.Transparent
        },
        contentColor = if (isSelected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimensionSize.size12,
                        vertical = MaterialTheme.dimensionSize.size12,
                    ),
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * Options with their labels already resolved, so callers can build [optionLabel] values with
 * `stringResource` and still hand [OptionSelector] and [OptionGroup] a plain label function.
 *
 * Selection is deliberately not part of this: one [OptionSet] can back several selectors that
 * track different selected values.
 */
@Immutable
class OptionSet<T> internal constructor(
    val values: List<T>,
    private val labels: Map<T, String>,
) {
    val label: (T) -> String = { option -> labels[option] ?: option.toString() }
}

@Composable
fun <T> optionSetOf(
    values: List<T>,
    label: @Composable (T) -> String,
): OptionSet<T> = OptionSet(values, values.associateWith { label(it) })

private const val EXPANDED_ROTATION = 180f
private const val DISABLED_ALPHA = 0.38f