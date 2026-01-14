package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.presenter.FavoritesViewModel
import com.practicum.playlistmaker.library.presenter.PlaylistAddViewModel
import com.practicum.playlistmaker.library.presenter.PlaylistViewModel
import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.search.presenter.SearchViewModel
import com.practicum.playlistmaker.setting.presenter.SettingViewModel
import com.practicum.playlistmaker.sharing.presenter.SharingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get(), get())
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

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        PlaylistAddViewModel(get(), get())
    }

}