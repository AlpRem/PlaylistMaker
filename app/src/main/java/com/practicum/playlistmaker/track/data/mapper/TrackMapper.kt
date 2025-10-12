package com.practicum.playlistmaker.track.data.mapper

import com.practicum.playlistmaker.common.component.EntityMapper
import com.practicum.playlistmaker.track.data.dto.TrackDto
import com.practicum.playlistmaker.track.domain.model.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.Locale

class TrackMapper: EntityMapper<TrackDto, Track> {

    override fun map(entity: TrackDto): Track {
        return Track(
            trackId = entity.trackId,
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTime = formatDuration(entity.trackTimeMillis),
            artworkUrl100 = entity.artworkUrl100,
            collectionName = entity.collectionName,
            releaseDate = extractYear(entity.releaseDate),
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            previewUrl = entity.previewUrl
        )
    }

    fun formatDuration(millis: Long?): String {
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