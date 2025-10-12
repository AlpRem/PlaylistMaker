package com.practicum.playlistmaker.track.domain.model

import com.practicum.playlistmaker.common.component.Page

data class TrackState (
    val page: Page<Track>,
    val isLoading: Boolean,
    val isError: Boolean,
    val isEmpty: Boolean
)