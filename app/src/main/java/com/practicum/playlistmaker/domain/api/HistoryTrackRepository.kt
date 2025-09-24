package com.practicum.playlistmaker.domain.api

import android.content.SharedPreferences
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.domain.model.Track

interface HistoryTrackRepository {
    fun getHistory(): Page<Track>
    fun setHistory(track: Track)
    fun cleanHistory()
}