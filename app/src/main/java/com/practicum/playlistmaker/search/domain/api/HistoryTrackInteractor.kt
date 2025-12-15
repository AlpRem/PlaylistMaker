package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface HistoryTrackInteractor {
    fun getHistory(): Flow<Page<Track>>
    fun saveTrack(track: Track)
    fun clearHistory()
}