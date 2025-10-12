package com.practicum.playlistmaker.track.data.network

import com.practicum.playlistmaker.track.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}