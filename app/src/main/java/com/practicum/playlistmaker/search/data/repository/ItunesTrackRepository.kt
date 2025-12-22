package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapperDto
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ItunesTrackRepository(private val networkClient: NetworkClient,
                            private val mapper: TrackMapperDto) : TrackRepository {

    override fun getTracks(query: String): Flow<Page<Track>> = flow {
        if (query.isBlank()) {
            emit(Page.empty())
            return@flow
        }
        val response = networkClient.doRequest(TracksSearchRequest(query))

        if (response.resultCode == 200) {
            emit(Page.of(mapper.mapList((response as TracksSearchResponse).results)))
        } else {
            emit(Page.withError("Server error: ${response.resultCode}"))
        }
    }
}