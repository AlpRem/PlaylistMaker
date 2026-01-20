package com.practicum.playlistmaker.library.domain.model

import com.practicum.playlistmaker.common.component.Page

data class PlaylistState(
    val page: Page<Playlist>,
    val isLoading: Boolean,
    val isError: Boolean,
    val isEmpty: Boolean
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
            isEmpty = false
        )

        fun empty() = PlaylistState(
            Page.empty(),
            isLoading = false,
            isError = false,
            isEmpty = true
        )

        fun content(page: Page<Playlist>) =
            PlaylistState(
                page,
                isLoading = false,
                isError = false,
                isEmpty = page.isEmpty()
            )

        fun clear() =
            PlaylistState(
                Page.empty(),
                isLoading = false,
                isError = false,
                isEmpty = false
            )
    }
}