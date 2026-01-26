package com.practicum.playlistmaker.db.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.domain.model.AddTrackToPlaylistResult
import com.practicum.playlistmaker.db.domain.model.PlaylistDetails
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistDbInteractor {
    fun list(): Flow<Page<Playlist>>
    suspend fun save(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): AddTrackToPlaylistResult

    suspend fun findById(id: Long): PlaylistDetails?
}