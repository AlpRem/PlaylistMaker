package com.practicum.playlistmaker.player.domain.model

sealed class AudioPlayerState {
    object Default : AudioPlayerState()
    object Prepared : AudioPlayerState()
    object Playing : AudioPlayerState()
    object Paused : AudioPlayerState()
}