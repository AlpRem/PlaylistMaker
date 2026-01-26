package com.practicum.playlistmaker.db.domain.model

import com.practicum.playlistmaker.library.domain.model.Playlist

data class PlaylistDetails (
    val playlist: Playlist,
    val totalDurationMillis: Long
)