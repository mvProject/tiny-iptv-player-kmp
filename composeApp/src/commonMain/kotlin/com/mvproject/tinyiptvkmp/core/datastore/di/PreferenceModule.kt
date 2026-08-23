package com.mvproject.tinyiptvkmp.core.datastore.di

import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import org.koin.dsl.module

val preferenceModule =
    module {
        single {
            PreferenceRepository(get())
        }
    }
