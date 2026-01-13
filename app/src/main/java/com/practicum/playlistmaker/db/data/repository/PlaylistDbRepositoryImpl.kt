package com.practicum.playlistmaker.db.data.repository

import com.google.gson.Gson
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.db.domain.api.PlaylistDbRepository
import com.practicum.playlistmaker.db.mapper.PlaylistMapperDao
import com.practicum.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistDbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapperDao,
    private val gson: Gson
): PlaylistDbRepository {
    override fun list(): Flow<Page<Playlist>> {
        return appDatabase
            .playlistDao()
            .list()
            .map { p -> Page.of(p.asReversed().map {playlistMapper.map(it)}) }
    }

    override suspend fun save(playlist: Playlist) {
        appDatabase.playlistDao().save(playlistMapper.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        val playlistDao = appDatabase.playlistDao()
        val playlistEntity = playlistDao.findById(playlistId) ?: return
        val trackIds = gson.fromJson(
            playlistEntity.tracksIds,
            Array<Long>::class.java
        ).toMutableList()

        if (trackIds.contains(trackId)) return
        trackIds.add(trackId)

        val updatedPlaylist = playlistEntity.copy(
            tracksIds = gson.toJson(trackIds),
            countTracks = trackIds.size
        )

        playlistDao.update(updatedPlaylist)
    }
}