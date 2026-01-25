package com.practicum.playlistmaker.db.data.repository

import com.google.gson.Gson
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.db.data.dao.PlaylistDao
import com.practicum.playlistmaker.db.domain.api.PlaylistDbRepository
import com.practicum.playlistmaker.db.domain.model.AddTrackToPlaylistResult
import com.practicum.playlistmaker.db.mapper.PlaylistMapperDao
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PlaylistDbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapperDao,
    private val gson: Gson
): PlaylistDbRepository {
    override fun list(): Flow<Page<Playlist>> {
        return appDatabase.playlistDao()
            .listPlaylist()
            .map { p -> Page.of(p.asReversed().map {playlistMapper.map(it)}) }
            .distinctUntilChanged()
    }

    override suspend fun save(playlist: Playlist) {
        appDatabase.playlistDao().save(playlistMapper.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: String): AddTrackToPlaylistResult {
        val playlistDao = appDatabase.playlistDao()
        val playlistEntity = playlistDao.findById(playlistId) ?: return AddTrackToPlaylistResult.TrackIsExists
        val trackIds: MutableList<String> =
            if (playlistEntity.tracksIds.isNotEmpty()) {
                gson.fromJson(
                    playlistEntity.tracksIds,
                    Array<String>::class.java
                ).toMutableList()
            } else {
                mutableListOf()
            }

        if (trackIds.contains(trackId))
            return AddTrackToPlaylistResult.TrackIsExists
        trackIds.add(trackId)

        val updatedPlaylist = playlistEntity.copy(
            tracksIds = gson.toJson(trackIds),
            countTracks = trackIds.size
        )

        playlistDao.update(updatedPlaylist)
        return AddTrackToPlaylistResult.ToAdded
    }

    override suspend fun findById(id: Long): Playlist? {
        return  appDatabase.playlistDao().findById(id)?.let { playlistMapper.map(it) }
    }
}