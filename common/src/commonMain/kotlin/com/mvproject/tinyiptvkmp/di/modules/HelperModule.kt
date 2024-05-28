/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 09.05.24, 19:27
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateHelper
import com.mvproject.tinyiptvkmp.data.helpers.PlaylistHelper
import com.mvproject.tinyiptvkmp.data.helpers.ViewTypeHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule =
    module {
        singleOf(::ViewTypeHelper)
        singleOf(::PlaylistHelper)
        singleOf(::DataUpdateHelper)
    }
