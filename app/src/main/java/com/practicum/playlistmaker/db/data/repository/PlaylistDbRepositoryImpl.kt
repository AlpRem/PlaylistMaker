package com.practicum.playlistmaker.db.data.repository

import com.google.gson.Gson
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.db.data.dao.PlaylistDao
import com.practicum.playlistmaker.db.domain.api.PlaylistDbRepository
import com.practicum.playlistmaker.db.domain.model.AddTrackToPlaylistResult
import com.practicum.playlistmaker.db.domain.model.PlaylistDetails
import com.practicum.playlistmaker.db.mapper.PlaylistMapperDao
import com.practicum.playlistmaker.db.mapper.TrackMapperDao
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PlaylistDbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapperDao,
    private val trackMapper: TrackMapperDao,
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

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): AddTrackToPlaylistResult {
        val playlistDao = appDatabase.playlistDao()
        val trackDao = appDatabase.trackDao()
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

        if (trackIds.contains(track.trackId))
            return AddTrackToPlaylistResult.TrackIsExists

        val trackInDB = trackDao.findById(track.trackId)
        if (trackInDB == null)
            trackDao.save(
                trackMapper.map(
                    track.copy(isPlaylist = true)
                )
            )
        else
            trackDao.updatePlaylist(track.trackId, true)

        trackIds.add(track.trackId)
        val updatedPlaylist = playlistEntity.copy(
            tracksIds = gson.toJson(trackIds),
            countTracks = trackIds.size
        )

        playlistDao.update(updatedPlaylist)
        return AddTrackToPlaylistResult.ToAdded
    }

    override suspend fun deleteTrackFromPlaylist(
        playlistId: Long,
        track: Track
    ) {
        val playlistDao = appDatabase.playlistDao()
        val trackDao = appDatabase.trackDao()
        val playlist = playlistDao.findById(playlistId) ?: return
        val trackIds = if (playlist.tracksIds.isNotEmpty())
            gson.fromJson(playlist.tracksIds, Array<String>::class.java).toMutableList()
        else
            mutableListOf()
        if (!trackIds.remove(track.trackId)) return
        playlistDao.update(
            playlist.copy(
                tracksIds = gson.toJson(trackIds),
                countTracks = trackIds.size
            )
        )

        val allPlaylists = playlistDao.listPlaylist()
        var isUseInPlaylists = false
        allPlaylists.collect { playlists ->
            isUseInPlaylists = playlists.any { p ->
                if (p.id == playlistId || p.tracksIds.isEmpty()) return@any false
                val ids = gson.fromJson(p.tracksIds, Array<String>::class.java)
                ids.contains(track.trackId)
            }
        }
        val trackDB = trackDao.findById(track.trackId) ?: return
        when {
            trackDB.isFavorite -> trackDao.updatePlaylist(track.trackId, isUseInPlaylists)
            isUseInPlaylists -> trackDao.updatePlaylist(track.trackId, true)
            else -> trackDao.delete(trackDB)
        }

    }

    override suspend fun deletePlaylist(playlistId: Long, tracks: List<Track>) {
        val playlistDao = appDatabase.playlistDao()
        val trackDao = appDatabase.trackDao()
        val playlist = playlistDao.findById(playlistId) ?: return
        val playlistsSnapshot = playlistDao.listPlaylist().first()
        for (track in tracks) {
            val isUseInPlaylists = playlistsSnapshot.any { p ->
                if (p.id == playlistId || p.tracksIds.isEmpty()) return@any false
                val ids = gson.fromJson(p.tracksIds, Array<String>::class.java)
                ids.contains(track.trackId)
            }
            val trackDb = trackDao.findById(track.trackId) ?: continue
            when {
                trackDb.isFavorite -> trackDao.updatePlaylist(track.trackId, isUseInPlaylists)
                isUseInPlaylists -> trackDao.updatePlaylist(track.trackId, true)
                else -> trackDao.delete(trackDb)
            }
        }
        playlistDao.delete(playlist)
    }

    override suspend fun findById(id: Long): PlaylistDetails? {
        val playlist = appDatabase.playlistDao().findById(id) ?: return null
        val trackIds = if (playlist.tracksIds.isNotEmpty()) {
            gson.fromJson(
                playlist.tracksIds,
                Array<String>::class.java
            ).toList()
        } else {
            emptyList()
        }
        val trackDB  = appDatabase
            .trackDao()
            .findByIds(trackIds)
        val tracks = trackDB.map { trackMapper.map(it) }
        val totalDuration = trackDB .sumOf { it.trackTimeMillis ?: 0L }

        return PlaylistDetails(
            playlist = playlistMapper.map(playlist),
            tracks = tracks,
            totalDurationMillis = totalDuration
        )
    }
}