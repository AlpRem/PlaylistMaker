package com.practicum.playlistmaker.library.domain

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track

data class FavoritesState (
    val page: Page<Track>,
    val isLoading: Boolean,
    val isEmpty: Boolean
) {

    companion object {

        fun loading() = FavoritesState(
            Page.empty(),
            isLoading = true,
            isEmpty = false
        )

        fun empty() = FavoritesState(
            Page.empty(),
            isLoading = false,
            isEmpty = true
        )

        fun content(page: Page<Track>) = FavoritesState(
            page,
            isLoading = false,
            isEmpty = false
        )
    }
}