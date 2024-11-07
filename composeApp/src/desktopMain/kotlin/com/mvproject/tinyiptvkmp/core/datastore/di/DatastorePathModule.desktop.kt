/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2024
 *  last modified : 29.07.24, 14:43
 *
 */

package com.mvproject.tinyiptvkmp.core.datastore.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun datastorePathModule(): Module =
    module {
        single {
            { "${System.getProperty("java.io.tmpdir")}/$dataStoreFileName" }
        }
    }
