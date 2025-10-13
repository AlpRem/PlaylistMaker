package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(page: Page<Track>)
    }
}