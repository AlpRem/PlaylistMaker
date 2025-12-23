package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.db.data.repository.TrackDbRepositoryImpl
import com.practicum.playlistmaker.db.domain.api.TrackDbRepository
import com.practicum.playlistmaker.db.mapper.TrackMapperDao
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

    factory { TrackMapperDao() }

    single<HistoryTrackRepository> {
        HistoryTrackRepositoryImpl(get(), get(), get())
    }

    single<TrackRepository> {
        ItunesTrackRepository(get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<TrackDbRepository> {
        TrackDbRepositoryImpl(get(), get())
    }
}