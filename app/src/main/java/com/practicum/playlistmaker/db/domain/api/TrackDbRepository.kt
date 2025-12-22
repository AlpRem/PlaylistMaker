package com.practicum.playlistmaker.db.domain.api

import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface  TrackDbRepository {
    fun list(): Flow<List<Track>>
    suspend fun save(track: TrackEntity)
    suspend fun delete(id: String)
}