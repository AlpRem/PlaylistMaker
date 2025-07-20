package com.practicum.playlistmaker.track.repository

import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.track.model.Track

interface TrackRepository{
    fun getTracks(query: String, callback: (Page<Track>) -> Unit)
}