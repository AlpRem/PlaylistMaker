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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class ItunesTrackRepository(private val networkClient: NetworkClient,
                            private val mapper: TrackMapperDto,
                            private val appDatabase: AppDatabase) : TrackRepository {

    override fun getTracks(query: String): Flow<Page<Track>> = combine(
        flow {
            if (query.isBlank()) {
                emit(Page.empty())
                return@flow
            }
            val response = networkClient.doRequest(TracksSearchRequest(query))
            if (response.resultCode == 200) {
                val tracks = mapper.mapList((response as TracksSearchResponse).results)
                emit(Page.of(tracks))
            } else {
                emit(Page.withError("Server error: ${response.resultCode}"))
            }
        },appDatabase.trackDao().findByFavoriteAllIds()
    ) {page, favoriteIds ->
        if (page.hasErrors() || page.isEmpty())
            page
        else {
            val favoriteSet = favoriteIds.toSet()
            val updatedTracks = page.data.map { track -> track.copy(isFavorite = favoriteSet.contains(track.trackId))}
            Page.of(updatedTracks)
        }
    }
}