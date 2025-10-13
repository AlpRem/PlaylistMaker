package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.model.Track

class ItunesTrackRepository(private val networkClient: NetworkClient,
                            private val mapper: TrackMapper) : TrackRepository {

    override fun getTracks(query: String): Page<Track> {
        if (query.isBlank()) {
            return Page.empty()
        }
        val response = networkClient.doRequest(TracksSearchRequest(query))

        return if (response.resultCode == 200) {
            Page.of(mapper.mapList((response as TracksSearchResponse).results))
        } else {
            Page.withError("Server error: ${response.resultCode}")
        }
    }
}