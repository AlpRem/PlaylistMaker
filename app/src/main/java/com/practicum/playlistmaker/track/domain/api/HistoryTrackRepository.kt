package com.practicum.playlistmaker.track.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.track.domain.model.Track

interface HistoryTrackRepository {
    fun getHistory(): Page<Track>
    fun setHistory(track: Track)
    fun cleanHistory()
}