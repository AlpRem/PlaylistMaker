package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track

interface HistoryTrackInteractor {
    fun getHistory(consumer: (Page<Track>) -> Unit)
    fun saveTrack(track: Track)
    fun clearHistory()
}