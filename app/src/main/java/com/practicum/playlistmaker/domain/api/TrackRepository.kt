package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.domain.model.Track

interface TrackRepository{
    fun getTracks(query: String): Page<Track>
}