package com.practicum.playlistmaker.player.domain.model

data class AddTrackToPlaylistState (
    val isAdded: Boolean,
    val isExists: Boolean,
    val playlistName: String
) {
    companion object {
        fun added(playlistName: String) =
            AddTrackToPlaylistState(
                isAdded = true,
                isExists = false,
                playlistName = playlistName
            )

        fun exists(playlistName: String) =
            AddTrackToPlaylistState(
                isAdded = false,
                isExists = true,
                playlistName = playlistName
            )

        fun clear() =
            AddTrackToPlaylistState(
                isAdded = false,
                isExists = false,
                playlistName = ""
            )
    }
}