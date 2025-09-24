package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.mapper.TrackMapper
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.model.Track

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