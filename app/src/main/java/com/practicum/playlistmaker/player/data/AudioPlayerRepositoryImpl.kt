package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    private var playerState: PlayerState = PlayerState.Default

    override fun preparePlayer(track: Track, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.Prepared
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.Prepared
            onCompletion()
        }
    }

    override fun startPlayer(onStart: () -> Unit) {
        mediaPlayer.start()
        playerState = PlayerState.Playing
        onStart()
    }

    override fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.pause()
        playerState = PlayerState.Paused
        onPause()
    }

    override fun getAudioPlayerState(): PlayerState = playerState
    override fun isPlaying(): Boolean {
        return playerState == PlayerState.Playing
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}