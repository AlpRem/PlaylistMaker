package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track

interface AudioPlayerRepository {
    fun preparePlayer(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun getAudioPlayerState(): PlayerState
    fun isPlaying(): Boolean
    fun currentPosition(): Int
}