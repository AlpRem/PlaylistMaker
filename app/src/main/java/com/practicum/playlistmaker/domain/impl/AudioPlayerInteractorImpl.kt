package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.model.AudioPlayerState
import com.practicum.playlistmaker.domain.model.Track

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) : AudioPlayerInteractor {
    override fun preparePlayer(
        track: Track,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        audioPlayerRepository.preparePlayer(track, onPrepared, onCompletion)
    }

    override fun startPlayer(onStart: () -> Unit) {
        audioPlayerRepository.startPlayer(onStart)
    }

    override fun pausePlayer(onPause: () -> Unit) {
        audioPlayerRepository.pausePlayer(onPause)
    }

    override fun getAudioPlayerState(): AudioPlayerState {
        return audioPlayerRepository.getAudioPlayerState()
    }

    override fun isPlaying(): Boolean {
        return audioPlayerRepository.isPlaying()
    }

    override fun currentPosition(): Int {
        return audioPlayerRepository.currentPosition()
    }

}