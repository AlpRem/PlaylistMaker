package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.presenter.AudioPlayerViewModel
import com.practicum.playlistmaker.player.presenter.LikeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        LikeViewModel()
    }
}