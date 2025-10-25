package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryTrackInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    factory<HistoryTrackInteractor> {
        HistoryTrackInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}