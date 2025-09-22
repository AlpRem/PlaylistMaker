package com.practicum.playlistmaker

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesClient
import com.practicum.playlistmaker.data.repository.HistoryTrackRepositoryImpl
import com.practicum.playlistmaker.data.repository.ItunesTrackRepository
import com.practicum.playlistmaker.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.HistoryTrackInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object  Creator {
    private fun getTracksRepository(): TrackRepository {
        return ItunesTrackRepository(ItunesClient(), TrackMapper())
    }

    private fun getHistoryTrackRepository(context: Context): HistoryTrackRepository {
        val prefs = context.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return HistoryTrackRepositoryImpl(prefs, Gson())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryTrackInteractor(context: Context): HistoryTrackInteractor {
        return HistoryTrackInteractorImpl(getHistoryTrackRepository(context))
    }
}