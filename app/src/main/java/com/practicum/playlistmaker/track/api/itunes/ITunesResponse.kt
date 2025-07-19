package com.practicum.playlistmaker.track.api.itunes


class ITunesResponse(val results: List<TrackDto>) {
    data class TrackDto(
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: Long?,
        val artworkUrl100: String
    )
}
