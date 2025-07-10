package com.practicum.playlistmaker.track.repository

import com.practicum.playlistmaker.track.model.Track

interface TrackRepository {
    fun getTracks(): List<Track>
}