package com.practicum.playlistmaker.db.domain.model

sealed class AddTrackToPlaylistResult {
    object ToAdded : AddTrackToPlaylistResult()
    object TrackIsExists : AddTrackToPlaylistResult()
}