package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.db.domain.api.PlaylistDbRepository
import com.practicum.playlistmaker.db.domain.model.AddTrackToPlaylistResult
import com.practicum.playlistmaker.db.domain.model.PlaylistDetails
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistDbInteractorImpl(private val playlistDbRepository: PlaylistDbRepository): PlaylistDbInteractor {
    override fun list(): Flow<Page<Playlist>> {
        return playlistDbRepository.list()
    }

    override suspend fun save(playlist: Playlist) {
        return playlistDbRepository.save(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): AddTrackToPlaylistResult {
        return playlistDbRepository.addTrackToPlaylist(playlistId, track)
    }

    override suspend fun deleteTrackToPlaylist(
        playlistId: Long,
        track: Track
    ) {
        return playlistDbRepository.deleteTrackFromPlaylist(playlistId, track)
    }

    override suspend fun findById(id: Long): PlaylistDetails? {
        return playlistDbRepository.findById(id)
    }
}