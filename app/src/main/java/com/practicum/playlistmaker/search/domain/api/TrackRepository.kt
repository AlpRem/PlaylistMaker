package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository{
    fun getTracks(query: String): Flow<Page<Track>>
}