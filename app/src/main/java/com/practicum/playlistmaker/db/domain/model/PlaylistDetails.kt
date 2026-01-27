package com.practicum.playlistmaker.db.domain.model

import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

data class PlaylistDetails (
    val playlist: Playlist,
    val tracks: List<Track>,
    val totalDurationMillis: Long
)