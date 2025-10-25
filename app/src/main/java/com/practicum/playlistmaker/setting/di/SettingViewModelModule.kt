package com.practicum.playlistmaker.setting.di

import com.practicum.playlistmaker.setting.presenter.SettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingViewModelModule = module {
    viewModel {
        SettingViewModel(get())
    }
}