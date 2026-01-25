package com.practicum.playlistmaker.search.data.mapper

import com.practicum.playlistmaker.common.component.EntityMapper
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.extractYear
import com.practicum.playlistmaker.util.formatDuration

class TrackMapperDto: EntityMapper<TrackDto, Track> {

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
            previewUrl = entity.previewUrl,
            isFavorite = entity.isFavorite,
            isPlaylist = entity.isPlaylist
        )
    }


}