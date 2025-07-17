package com.practicum.playlistmaker.itunes

import com.google.gson.annotations.SerializedName

class ITunesResponse(val results: TrackDto) {
    data class TrackDto(
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: Long?,
        val artworkUrl100: String
    )
}
