package com.practicum.playlistmaker.db.data.repository

import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.db.domain.api.TrackDbRepository
import com.practicum.playlistmaker.db.mapper.TrackMapperDao
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackDbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackMapper: TrackMapperDao,
): TrackDbRepository {
    override fun list(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().list()
        emit(convert(tracks))
    }

    override suspend fun save(track: TrackEntity) {
        appDatabase.trackDao().save(track)
    }

    override suspend fun delete(id: String) {
        val track = appDatabase.trackDao().findById(id)
        if (track != null)
            appDatabase.trackDao().delete(track)
    }

    private fun convert(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { movie -> trackMapper.map(movie) }
    }
}