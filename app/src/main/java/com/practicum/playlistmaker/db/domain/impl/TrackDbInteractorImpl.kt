package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.domain.api.TrackDbInteractor
import com.practicum.playlistmaker.db.domain.api.TrackDbRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackDbInteractorImpl(private val trackDbRepository: TrackDbRepository): TrackDbInteractor {
    override fun findByFavorite(): Flow<Page<Track>> {
        return trackDbRepository.findByFavorite()
    }

    override fun findByPlaylist(): Flow<Page<Track>> {
        return trackDbRepository.findByPlaylist()
    }

    override suspend fun save(track: Track) {
        return trackDbRepository.save(track)
    }

    private suspend fun garbageCollector(track: Track) {
        if (!track.isFavorite && !track.isPlaylist) {
            trackDbRepository.delete(track.trackId)
        }
    }

}