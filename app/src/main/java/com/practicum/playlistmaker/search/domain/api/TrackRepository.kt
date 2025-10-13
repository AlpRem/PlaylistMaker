package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track

interface TrackRepository{
    fun getTracks(query: String): Page<Track>
}