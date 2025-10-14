package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
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