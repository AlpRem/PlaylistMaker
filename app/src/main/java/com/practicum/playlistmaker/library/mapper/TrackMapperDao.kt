package com.practicum.playlistmaker.library.mapper

import com.practicum.playlistmaker.library.db.entity.TrackEntity
import com.practicum.playlistmaker.util.formatDuration
import com.practicum.playlistmaker.search.domain.model.Track

class TrackMapperDao {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId, track.trackName, track.artistName,
            formatDuration(track.trackTime), track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.id, track.trackName, track.artistName, formatDuration(track.trackTimeMillis),
            track.artworkUrl100, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.previewUrl
        )
    }
}