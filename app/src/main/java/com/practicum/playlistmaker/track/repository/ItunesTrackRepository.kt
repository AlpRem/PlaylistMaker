package com.practicum.playlistmaker.track.repository

import com.google.gson.Gson
import com.practicum.playlistmaker.component.Meta
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.component.mapper.TrackDtoMapper
import com.practicum.playlistmaker.track.api.itunes.ITunesResponse
import com.practicum.playlistmaker.track.api.itunes.ItunesClient
import com.practicum.playlistmaker.track.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItunesTrackRepository : TrackRepository{

    private val mapper = TrackDtoMapper()
    override fun getTracks(query: String, callback: (Page<Track>) -> Unit) {
        searchTracks(query, callback)
    }

    private fun searchTracks(query: String, callback: (Page<Track>) -> Unit) {
        if (query.isBlank()) {
            callback(Page.empty())
            return
        }

        ItunesClient.itunesService.search(query)
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    val page = if (response.isSuccessful) {
                        response.body()?.let {dto ->
                            Page(meta = Meta(
                                count = dto.results.size,
                                errors = emptyList()
                            ),
                                data = mapper.mapList(dto.results))
                        } ?: Page.empty()
                    } else {
                        Page.withError("Server error: ${response.code()}")
                    }
                    callback(page)
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    callback(Page.withError("Network error: ${t.localizedMessage}"))
                }
            })
    }
}