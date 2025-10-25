package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryTrackInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    factory<HistoryTrackInteractor> {
        HistoryTrackInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }
}