package com.practicum.playlistmaker.db.mapper

import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity
import com.practicum.playlistmaker.util.formatDuration
import com.practicum.playlistmaker.search.domain.model.Track

class TrackMapperDao {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId, track.trackName, track.artistName,
            formatDuration(track.trackTime), track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl,
            track.isFavorite)
    }

    fun mapToTrackInPlaylistEntity(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId, track.trackName, track.artistName,
            formatDuration(track.trackTime), track.artworkUrl100, track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country, track.previewUrl
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.id, track.trackName, track.artistName, formatDuration(track.trackTimeMillis),
            track.artworkUrl100, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.previewUrl, track.isFavorite)
    }

    fun map(track: TrackInPlaylistEntity): Track {
        return Track(
            track.id, track.trackName, track.artistName, formatDuration(track.trackTimeMillis),
            track.artworkUrl100, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.previewUrl, false)
    }
}