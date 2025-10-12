package com.practicum.playlistmaker.track.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.track.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: Page<Track>)
    }
}