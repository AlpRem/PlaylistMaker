package com.practicum.playlistmaker.track.data.repository

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.track.data.network.NetworkClient
import com.practicum.playlistmaker.track.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.track.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.track.data.mapper.TrackMapper
import com.practicum.playlistmaker.track.domain.api.TrackRepository
import com.practicum.playlistmaker.track.domain.model.Track

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