/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 26.07.24, 13:31
 *
 */

package com.mvproject.tinyiptvkmp.di.modules

import com.mvproject.tinyiptvkmp.data.helpers.DataUpdateHelper
import com.mvproject.tinyiptvkmp.data.helpers.GroupHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule =
    module {
        singleOf(::DataUpdateHelper)
        singleOf(::GroupHelper)
    }
