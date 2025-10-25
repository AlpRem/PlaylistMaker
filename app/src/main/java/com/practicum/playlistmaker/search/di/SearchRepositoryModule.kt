package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.data.repository.HistoryTrackRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.ItunesTrackRepository
import com.practicum.playlistmaker.search.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module

val searchRepositoryModule = module {

    single<HistoryTrackRepository> {
        HistoryTrackRepositoryImpl(get(), get())
    }

    single<TrackRepository> {
        ItunesTrackRepository(get(), get())
    }
}