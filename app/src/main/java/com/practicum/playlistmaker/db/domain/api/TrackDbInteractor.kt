package com.practicum.playlistmaker.db.domain.api

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackDbInteractor {
    fun list(): Flow<List<Track>>
}