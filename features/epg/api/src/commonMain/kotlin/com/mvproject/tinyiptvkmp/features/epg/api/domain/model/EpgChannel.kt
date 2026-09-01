/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 27.06.24, 14:40
 *
 */

package com.mvproject.tinyiptvkmp.features.epg.api.domain.model

data class EpgChannel(
    val id: String,
    val programId: String,
    val title: String,
    val logo: String = "",
)
