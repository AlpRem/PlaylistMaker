package com.practicum.playlistmaker.library.domain.model

data class PlaylistAddState (
    val playlistId: Long? = null,
    val isAddPlaylistBtnEnabled: Boolean =  false,
    val namePlaylist: String = "",
    val oldNamePlaylist: String = "",
    val descriptionPlaylist: String? = null,
    val oldDescriptionPlaylist: String? = null,
    val coverPlaylistUri: String? = null,
    val oldCoverPlaylistUri: String? = null,
    val tracksIds: String = "[]",
    val countTracks: Int = 0,
    val isSaveSuccess: Boolean = false,
    val isSaveError: Boolean = false
) {

    val isEditPlaylist: Boolean
        get() = playlistId != null
    val isChangeData: Boolean
        get() = namePlaylist != oldNamePlaylist ||
                descriptionPlaylist != oldDescriptionPlaylist ||
                coverPlaylistUri != oldCoverPlaylistUri
}