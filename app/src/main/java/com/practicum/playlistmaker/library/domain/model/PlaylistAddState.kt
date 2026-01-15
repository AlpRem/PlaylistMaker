package com.practicum.playlistmaker.library.domain.model

data class PlaylistAddState (
    val isAddPlaylistBtnEnabled: Boolean =  false,
    val namePlaylist: String = "",
    val descriptionPlaylist: String? = null,
    val coverPlaylistUri: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSaveError: Boolean = false
) {
    val isChangeData: Boolean
        get() = namePlaylist.isNotBlank() ||
                !descriptionPlaylist.isNullOrBlank() ||
                !coverPlaylistUri.isNullOrBlank()
}