package com.practicum.playlistmaker.track.domain.model

import com.practicum.playlistmaker.common.component.Page

data class TrackState (
    val page: Page<Track>,
    val isLoading: Boolean,
    val isError: Boolean,
    val isEmpty: Boolean,
    val isHistory: Boolean
) {
    companion object {
        fun loading() = TrackState(
            Page.empty(),
            isLoading = true,
            isError = false,
            isEmpty = false,
            isHistory = false
        )

        fun error() = TrackState(
            Page.empty(),
            isLoading = false,
            isError = true,
            isEmpty = false,
            isHistory = false
        )

        fun empty() = TrackState(
            Page.empty(),
            isLoading = false,
            isError = false,
            isEmpty = true,
            isHistory = false
        )

        fun content(page: Page<Track>, isHistory: Boolean = false) =
            TrackState(
                page,
                isLoading = false,
                isError = false,
                isEmpty = page.isEmpty(),
                isHistory = isHistory
            )

        fun clear() =
            TrackState(
                Page.empty(),
                isLoading = false,
                isError = false,
                isEmpty = false,
                isHistory = false
            )
    }
}