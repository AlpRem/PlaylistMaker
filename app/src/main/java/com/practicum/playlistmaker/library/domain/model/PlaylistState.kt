package com.practicum.playlistmaker.library.domain.model

import com.practicum.playlistmaker.common.component.Page

data class PlaylistState(
    val page: Page<Playlist>,
    val isLoading: Boolean,
    val isError: Boolean,
    val isEmpty: Boolean,
    val openPlaylistId: Long? = null
) {
    companion object {

        fun loading() = PlaylistState(
            Page.empty(),
            isLoading = true,
            isError = false,
            isEmpty = false
        )

        fun error() = PlaylistState(
            Page.empty(),
            isLoading = false,
            isError = true,
            isEmpty = false,
            openPlaylistId = null
        )

        fun empty() = PlaylistState(
            Page.empty(),
            isLoading = false,
            isError = false,
            isEmpty = true,
            openPlaylistId = null
        )

        fun content(page: Page<Playlist>) =
            PlaylistState(
                page,
                isLoading = false,
                isError = false,
                isEmpty = page.isEmpty(),
                openPlaylistId = null
            )

        fun openPlaylist(page: Page<Playlist>, playlistId: Long) =
            PlaylistState(
                page,
                isLoading = false,
                isError = false,
                isEmpty = page.isEmpty(),
                openPlaylistId = playlistId
            )

        fun clear() =
            PlaylistState(
                Page.empty(),
                isLoading = false,
                isError = false,
                isEmpty = false,
                openPlaylistId = null
            )
    }
}