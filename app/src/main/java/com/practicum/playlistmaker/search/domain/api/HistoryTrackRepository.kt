package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface HistoryTrackRepository {
    fun getHistory(): Flow<Page<Track>>
    fun setHistory(track: Track)
    fun cleanHistory()
}