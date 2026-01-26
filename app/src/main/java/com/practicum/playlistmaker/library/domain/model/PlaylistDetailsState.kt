package com.practicum.playlistmaker.library.domain.model

data class PlaylistDetailsState (
    val isLoading: Boolean = false,
    val playlist: Playlist? = null,
    val isEmpty: Boolean = false,
    val totalDurationMillis: Long = 0L
)