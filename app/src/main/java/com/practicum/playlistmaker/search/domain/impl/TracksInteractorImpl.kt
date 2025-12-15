package com.practicum.playlistmaker.search.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow


class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Page<Track>> {
        return repository.getTracks(expression)
    }
}