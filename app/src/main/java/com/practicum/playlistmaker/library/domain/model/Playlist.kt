package com.practicum.playlistmaker.library.domain.model

data class Playlist (
    val id: Long,
    val name: String,
    val description: String = "",
    val image: String = "",
    val tracksIds: String = "[]",
    val countTracks: Int = 0
)