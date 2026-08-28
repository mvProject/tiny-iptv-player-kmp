package com.mvproject.tinyiptvkmp.features.groups.di

import com.mvproject.tinyiptvkmp.features.groups.GroupViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val groupsModule = module {
    viewModel<GroupViewModel>()
}
