package com.practicum.playlistmaker.library.domain.model

import com.practicum.playlistmaker.search.domain.model.Track

data class PlaylistDetailsState (
    val isLoading: Boolean = false,
    val playlist: Playlist? = null,
    val tracks: List<Track> = emptyList(),
    val isEmpty: Boolean = false,
    val totalDurationMillis: Long = 0L,
    val playerTrack: Track? = null,
    val completerDelete: Boolean = false
)