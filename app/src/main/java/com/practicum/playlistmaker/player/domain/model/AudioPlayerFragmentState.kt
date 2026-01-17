package com.practicum.playlistmaker.player.domain.model

import com.practicum.playlistmaker.library.domain.model.PlaylistState

data class AudioPlayerFragmentState (
    val audioPlayerState: AudioPlayerState = AudioPlayerState(),
    val playlistState: PlaylistState = PlaylistState.clear()
)