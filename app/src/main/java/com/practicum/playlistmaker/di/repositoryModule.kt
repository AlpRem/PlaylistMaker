package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.search.data.repository.HistoryTrackRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.ItunesTrackRepository
import com.practicum.playlistmaker.search.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.setting.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<HistoryTrackRepository> {
        HistoryTrackRepositoryImpl(get(), get())
    }

    single<TrackRepository> {
        ItunesTrackRepository(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}