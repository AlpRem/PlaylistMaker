package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.model.AudioPlayerState
import com.practicum.playlistmaker.domain.model.Track

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    private var audioPlayerState: AudioPlayerState = AudioPlayerState.Default

    override fun preparePlayer(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            audioPlayerState = AudioPlayerState.Prepared
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            audioPlayerState = AudioPlayerState.Prepared
            onCompletion()
        }
    }

    override fun startPlayer(onStart: () -> Unit) {
        mediaPlayer.start()
        audioPlayerState = AudioPlayerState.Playing
        onStart()
    }

    override fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.pause()
        audioPlayerState = AudioPlayerState.Paused
        onPause()
    }

    override fun getAudioPlayerState(): AudioPlayerState = audioPlayerState
    override fun isPlaying(): Boolean {
        return audioPlayerState == AudioPlayerState.Playing
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}