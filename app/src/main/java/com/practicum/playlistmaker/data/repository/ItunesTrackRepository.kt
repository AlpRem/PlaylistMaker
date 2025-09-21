package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.component.Meta
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.Locale

class ItunesTrackRepository(private val networkClient: NetworkClient) : TrackRepository {

    override fun getTracks(query: String): Page<Track> {
        if (query.isBlank()) {
            return Page.empty()
        }
        val response = networkClient.doRequest(TracksSearchRequest(query))

        if (response.resultCode == 200) {
            val tracks = (response as TracksSearchResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName,
                    formatDuration(it.trackTimeMillis), it.artworkUrl100,
                    it.collectionName, extractYear(it.releaseDate),
                    it.primaryGenreName, it.country,it.previewUrl)

            }
            return Page(meta = Meta(count = tracks.size, errors = emptyList()), data = tracks)
        } else {
            return Page.withError("Server error: ${response.resultCode}")
        }
    }

    private fun formatDuration(millis: Long?): String {
        return millis?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        } ?: "00:00"
    }

    fun extractYear(dateString: String?): String {
        if (dateString.isNullOrEmpty()) {
            return ""
        }
        return try {
            Instant.parse(dateString).atZone(ZoneId.systemDefault()).year.toString()
        } catch (e: DateTimeParseException) {
            ""
        }
    }
}