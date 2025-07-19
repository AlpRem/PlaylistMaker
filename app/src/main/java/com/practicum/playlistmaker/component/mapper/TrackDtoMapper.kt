package com.practicum.playlistmaker.component.mapper

import com.practicum.playlistmaker.component.EntityMapper
import com.practicum.playlistmaker.track.api.itunes.ITunesResponse
import com.practicum.playlistmaker.track.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDtoMapper : EntityMapper<ITunesResponse.TrackDto, Track>  {
    override fun map(entity: ITunesResponse.TrackDto): Track = Track(
        trackName = entity.trackName,
        artistName = entity.artistName,
        trackTime = formatDuration(entity.trackTimeMillis),
        artworkUrl100 = entity.artworkUrl100
    )

    private fun formatDuration(millis: Long?): String {
        return millis?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        } ?: "00:00"
    }
}