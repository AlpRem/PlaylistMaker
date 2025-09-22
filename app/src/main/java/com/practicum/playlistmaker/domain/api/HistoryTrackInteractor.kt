package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.domain.model.Track

interface HistoryTrackInteractor {
    fun getHistory(consumer: (Page<Track>) -> Unit)
    fun saveTrack(track: Track)
    fun clearHistory()
}