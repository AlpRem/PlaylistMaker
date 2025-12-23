package com.practicum.playlistmaker.player.domain.model

sealed class PlayerState {
    object Default : PlayerState()
    object Prepared : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}