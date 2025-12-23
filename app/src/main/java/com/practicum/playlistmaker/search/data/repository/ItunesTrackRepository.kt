package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapperDto
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ItunesTrackRepository(private val networkClient: NetworkClient,
                            private val mapper: TrackMapperDto,
                            private val appDatabase: AppDatabase) : TrackRepository {

    override fun getTracks(query: String): Flow<Page<Track>> = flow {
        if (query.isBlank()) {
            emit(Page.empty())
            return@flow
        }
        val response = networkClient.doRequest(TracksSearchRequest(query))

        if (response.resultCode == 200) {
            val tracks = mapper.mapList((response as TracksSearchResponse).results)
            val favoriteIds = appDatabase.trackDao().list().first(). map { it.id }.toSet()
            tracks.forEach { t ->
                t.isFavorite = favoriteIds.contains(t.trackId)
            }
            emit(Page.of(tracks))
        } else {
            emit(Page.withError("Server error: ${response.resultCode}"))
        }
    }
}