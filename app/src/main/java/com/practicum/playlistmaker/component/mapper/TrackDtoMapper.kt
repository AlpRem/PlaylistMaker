package com.practicum.playlistmaker.component.mapper

import com.practicum.playlistmaker.component.EntityMapper
import com.practicum.playlistmaker.track.api.itunes.ITunesResponse
import com.practicum.playlistmaker.track.model.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.Locale

class TrackDtoMapper : EntityMapper<ITunesResponse.TrackDto, Track>  {
    override fun map(entity: ITunesResponse.TrackDto): Track = Track(
        trackId =  entity.trackId,
        trackName = entity.trackName,
        artistName = entity.artistName,
        trackTime = formatDuration(entity.trackTimeMillis),
        artworkUrl100 = entity.artworkUrl100,
        collectionName = entity.collectionName,
        releaseDate = entity.releaseDate,
        primaryGenreName = extractYear(entity.primaryGenreName),
        country = entity.country
    )

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