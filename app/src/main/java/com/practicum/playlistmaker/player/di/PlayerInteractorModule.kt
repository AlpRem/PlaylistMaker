package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {
    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }
}