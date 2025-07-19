package com.practicum.playlistmaker.track.repository

import android.util.Log
import com.practicum.playlistmaker.component.Meta
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.itunes.ITunesResponse
import com.practicum.playlistmaker.itunes.ItunesClient
import com.practicum.playlistmaker.track.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ItunesTrackRepository : TrackRepository{


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
                        response.body()?.toTrackPage() ?: Page.empty()
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

    private fun ITunesResponse.toTrackPage(): Page<Track> {
        return Page(
            meta = Meta(
                count = results.size,
                errors = emptyList()
            ),
            data = results.map { it.toTrack() }
        )
    }

    private fun ITunesResponse.TrackDto.toTrack(): Track {
        return Track(
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = formatDuration(this.trackTimeMillis),
            artworkUrl100 = this.artworkUrl100
        )
    }

    private fun formatDuration(millis: Long?): String {
        if (millis == null) return "00:00"
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }
}