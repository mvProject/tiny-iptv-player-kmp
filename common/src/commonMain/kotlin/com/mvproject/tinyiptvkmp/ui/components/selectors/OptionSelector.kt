/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.ui.components.selectors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptvkmp.ui.theme.dimens
import com.mvproject.tinyiptvkmp.utils.AppConstants.EMPTY_STRING
import com.mvproject.tinyiptvkmp.utils.AppConstants.WEIGHT_1

@Composable
fun OptionSelector(
    modifier: Modifier = Modifier,
    title: String = EMPTY_STRING,
    enabled: Boolean = true,
    selectedItem: String = EMPTY_STRING,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        enabled = enabled,
        border = BorderStroke(
            width = MaterialTheme.dimens.size1,
            color = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(
            start = MaterialTheme.dimens.size12
        ),
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.dimens.font10
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
            }

            Text(
                text = selectedItem,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.weight(WEIGHT_1))

        val icon = if (isExpanded)
            Icons.Filled.ArrowDropUp
        else
            Icons.Filled.ArrowDropDown

        FilledIconButton(
            enabled = enabled,
            onClick = onClick,
            modifier = Modifier.padding(MaterialTheme.dimens.size8),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Close Icon",
            )
        }
    }
}