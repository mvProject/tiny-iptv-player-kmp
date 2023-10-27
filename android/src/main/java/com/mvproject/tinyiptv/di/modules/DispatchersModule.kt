/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.di.modules

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatchersModule = module {
    single(named("IODispatcher")) { Dispatchers.IO }
    single(named("DefaultDispatcher")) { Dispatchers.Default }
}