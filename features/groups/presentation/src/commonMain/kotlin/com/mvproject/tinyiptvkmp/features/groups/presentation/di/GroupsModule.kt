package com.mvproject.tinyiptvkmp.features.groups.presentation.di

import com.mvproject.tinyiptvkmp.features.groups.presentation.GroupViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val groupsModule = module {
    viewModel<GroupViewModel>()
}
