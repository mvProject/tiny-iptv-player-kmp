package com.mvproject.tinyiptvkmp.features.groups.di

import com.mvproject.tinyiptvkmp.features.groups.GroupViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val groupsModule = module {
    factoryOf(::GroupViewModel)
}