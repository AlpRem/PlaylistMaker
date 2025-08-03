package com.practicum.playlistmaker.track.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.track.model.Track

interface HistoryTrackRepository {
    fun getHistory(sharedPreferences: SharedPreferences, callback: (Page<Track>) -> Unit)
    fun setHistory(sharedPreferences: SharedPreferences, track: Track)
    fun cleanHistory(sharedPreferences: SharedPreferences)
}