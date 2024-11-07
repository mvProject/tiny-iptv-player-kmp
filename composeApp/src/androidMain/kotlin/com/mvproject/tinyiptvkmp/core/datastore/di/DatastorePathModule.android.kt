/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 20.11.23, 20:27
 *
 */

package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun datastorePathModule(): Module =
    module {
        single {
            { androidContext().filesDir.resolve(dataStoreFileName).absolutePath }
        }
    }
