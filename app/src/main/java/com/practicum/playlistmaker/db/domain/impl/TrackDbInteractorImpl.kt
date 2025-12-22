package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.domain.api.TrackDbInteractor
import com.practicum.playlistmaker.db.domain.api.TrackDbRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackDbInteractorImpl(private val trackDbRepository: TrackDbRepository): TrackDbInteractor {
    override fun list(): Flow<List<Track>> {
        return trackDbRepository.list().map { t ->
            t.reversed()
        }
    }

}