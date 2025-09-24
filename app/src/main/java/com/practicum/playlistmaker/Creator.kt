package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.data.network.ItunesClient
import com.practicum.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.HistoryTrackRepositoryImpl
import com.practicum.playlistmaker.data.repository.ItunesTrackRepository
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.HistoryTrackInteractorImpl
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
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

    private fun getSettingsRepository(context: Context): SettingsRepository {
        val prefs: SharedPreferences = context.getSharedPreferences(
            PLAYLIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )
        return SettingsRepositoryImpl(prefs)
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryTrackInteractor(context: Context): HistoryTrackInteractor {
        return HistoryTrackInteractorImpl(getHistoryTrackRepository(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun providerAudioPlayer(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }
}