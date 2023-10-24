/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.helpers.PlaylistHelper
import com.mvproject.tinyiptv.data.helpers.ViewTypeHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule = module {
    singleOf(::ViewTypeHelper)
    singleOf(::PlaylistHelper)
}