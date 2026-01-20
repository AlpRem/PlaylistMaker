package com.practicum.playlistmaker.db.mapper

import com.practicum.playlistmaker.db.data.entity.PlaylistEntity
import com.practicum.playlistmaker.library.domain.model.Playlist

class PlaylistMapperDao {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            image = playlist.image,
            tracksIds = playlist.tracksIds,
            countTracks = playlist.countTracks
        )
    }

    fun map(entity: PlaylistEntity): Playlist {
        return Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            image = entity.image,
            tracksIds = entity.tracksIds,
            countTracks = entity.countTracks
        )
    }
}