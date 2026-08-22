package com.mvproject.tinyiptvkmp.core.datastore.di

import com.mvproject.tinyiptvkmp.core.datastore.repository.PreferenceRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val preferenceModule =
    module {
        singleOf(::PreferenceRepository)
    }
