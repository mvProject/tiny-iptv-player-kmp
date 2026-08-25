/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 24.03.24, 10:49
 *
 */

package com.mvproject.tinyiptvkmp.core.components.toolbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptvkmp.core.theme.AppTheme
import com.mvproject.tinyiptvkmp.core.theme.colorSchemeExtended
import com.mvproject.tinyiptvkmp.core.theme.dimensionSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    placeholderText: String,
    onTextChange: (String) -> Unit = {},
    onCloseClicked: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            TextField(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                placeholder = {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                trailingIcon = {
                    FilledIconButton(
                        onClick = {
                            if (text.isNotEmpty()) {
                                onTextChange("")
                            } else {
                                onCloseClicked()
                            }
                        },
                        modifier = Modifier.padding(MaterialTheme.dimensionSize.size8),
                        colors =
                            IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                        )
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorSchemeExtended.activeInput,
                        focusedIndicatorColor = MaterialTheme.colorSchemeExtended.activeInput,
                    ),
            )
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchAppBarPreview() {
    AppTheme {
        SearchAppBar(
            text = "National Geographic",
            placeholderText = "type here...",
        )
    }
}
