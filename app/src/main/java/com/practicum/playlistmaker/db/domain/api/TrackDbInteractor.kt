package com.practicum.playlistmaker.db.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackDbInteractor {
    fun findByFavorite(): Flow<Page<Track>>

    fun findByPlaylist(): Flow<Page<Track>>
    suspend fun save(track: Track)
}