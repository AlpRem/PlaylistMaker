package com.practicum.playlistmaker.db.domain.model

sealed interface AddTrackToPlaylistResult {
    object ToAdded : AddTrackToPlaylistResult
    object TrackIsExists : AddTrackToPlaylistResult
}