package com.practicum.playlistmaker.player.domain.model

data class AudioPlayerState (
    val playerState: PlayerState = PlayerState.Default,
    val timerState: String = "00:00",
    val likeState: Boolean = false
)