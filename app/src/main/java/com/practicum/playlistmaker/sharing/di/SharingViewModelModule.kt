package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.presenter.SharingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharingViewModelModule = module {
    viewModel {
        SharingViewModel(get())
    }
}