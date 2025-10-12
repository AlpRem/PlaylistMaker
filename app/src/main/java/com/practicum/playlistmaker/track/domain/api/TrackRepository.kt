package com.practicum.playlistmaker.track.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.track.domain.model.Track

interface TrackRepository{
    fun getTracks(query: String): Page<Track>
}