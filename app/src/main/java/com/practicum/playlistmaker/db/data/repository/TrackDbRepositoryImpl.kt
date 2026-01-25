package com.practicum.playlistmaker.db.data.repository

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.db.domain.api.TrackDbRepository
import com.practicum.playlistmaker.db.mapper.TrackMapperDao
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackDbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackMapper: TrackMapperDao,
): TrackDbRepository {
    override fun findByFavorite(): Flow<Page<Track>> {
        return appDatabase
            .trackDao()
            .findByFavorite()
            .map { t -> Page.of(t.asReversed().map { trackMapper.map(it) }) }
    }

    override fun findByPlaylist(): Flow<Page<Track>> {
        return appDatabase
            .trackDao()
            .findByPlaylist()
            .map { t -> Page.of(t.asReversed().map { trackMapper.map(it) }) }
    }

    override suspend fun save(track: Track) {
        val dao = appDatabase.trackDao()
        val entity  = dao.findByIdAndFavorite(track.trackId)
        if (entity  == null)
            dao.save(trackMapper.map(track.copy(isFavorite = true)))
        else
            dao.updateFavorite(track.trackId, !entity .isFavorite)
    }

    override suspend fun delete(id: String) {
        val track = appDatabase.trackDao().findByIdAndFavorite(id)
        if (track != null)
            appDatabase.trackDao().delete(track)
    }
}