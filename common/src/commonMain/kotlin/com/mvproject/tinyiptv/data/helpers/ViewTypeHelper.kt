/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.helpers

import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.repository.PreferenceRepository

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