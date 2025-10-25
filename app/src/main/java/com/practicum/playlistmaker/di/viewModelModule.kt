package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.player.presenter.LikeViewModel
import com.practicum.playlistmaker.search.presenter.SearchViewModel
import com.practicum.playlistmaker.setting.presenter.SettingViewModel
import com.practicum.playlistmaker.sharing.presenter.SharingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        LikeViewModel()
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingViewModel(get())
    }

    viewModel {
        SharingViewModel(get())
    }

}