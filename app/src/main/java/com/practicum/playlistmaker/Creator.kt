package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.network.ItunesClient
import com.practicum.playlistmaker.data.repository.ItunesTrackRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object  Creator {
    private fun getTracksRepository(): TrackRepository {
        return ItunesTrackRepository(ItunesClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}