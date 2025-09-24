package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: Page<Track>)
    }
}