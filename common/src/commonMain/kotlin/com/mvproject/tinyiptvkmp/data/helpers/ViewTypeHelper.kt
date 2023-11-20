/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.data.helpers

import com.mvproject.tinyiptvkmp.data.enums.ChannelsViewType
import com.mvproject.tinyiptvkmp.data.repository.PreferenceRepository

class ViewTypeHelper(private val preferenceRepository: PreferenceRepository) {

    suspend fun setChannelsViewType(type: ChannelsViewType) {
        preferenceRepository.setChannelsViewType(type = type.name)
    }

    suspend fun getChannelsViewType(): ChannelsViewType {
        val currentType = preferenceRepository.getChannelsViewType()
        return if (currentType != null) {
            ChannelsViewType.valueOf(currentType)
        } else
            ChannelsViewType.LIST
    }
}