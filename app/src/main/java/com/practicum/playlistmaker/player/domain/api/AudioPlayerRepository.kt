package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.track.domain.model.Track

interface AudioPlayerRepository {
    fun preparePlayer(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun getAudioPlayerState(): AudioPlayerState
    fun isPlaying(): Boolean
    fun currentPosition(): Int
}