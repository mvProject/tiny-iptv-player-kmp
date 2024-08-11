/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 07.05.24, 17:36
 *
 */

package com.mvproject.tinyiptvkmp.ui.data

import androidx.compose.runtime.Immutable

@Immutable
data class Options(
    val items: List<String> = emptyList(),
)
