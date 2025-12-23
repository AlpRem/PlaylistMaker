package com.practicum.playlistmaker.player.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.TrackDbInteractor
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor,
    private val trackDbInteractor: TrackDbInteractor): ViewModel() {
    private val stateAudioPlayer = MutableLiveData(AudioPlayerState())
    val observeStateAudioPlayer: LiveData<AudioPlayerState> = stateAudioPlayer

    var track: Track? = null
        private set


    private var timerJob: Job? = null

    fun preparePlayer(track: Track) {
        this.track = track
        stateAudioPlayer.value = stateAudioPlayer.value?.copy(likeState = track.isFavorite)
        audioPlayerInteractor.preparePlayer(track,
            onPrepared = {
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(playerState = PlayerState.Prepared)

            },
            onCompletion = {
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(playerState = PlayerState.Prepared)
                stopTimer()
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(timerState = "00:00")
            })
    }

    fun playbackControl() {
        when (stateAudioPlayer.value?.playerState){
            PlayerState.Playing -> pausePlayer()
            else -> startPlayer()
        }
    }

    fun onPause() {
        pausePlayer()
    }

    fun toggleLike() {
        val currentTrack = track ?: return
        val oldLike = stateAudioPlayer.value?.likeState ?: false
        viewModelScope.launch {
            if (oldLike)
                trackDbInteractor.delete(currentTrack.trackId)
            else
                trackDbInteractor.save(currentTrack)
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(likeState = !oldLike)
            track = currentTrack.copy(isFavorite = !oldLike)
        }
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer(onStart = {
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(playerState = PlayerState.Playing)
            startTimer()
        })
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer(onPause = {
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(playerState = PlayerState.Paused)
            stopTimer()
        })
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.isPlaying()) {
                val timeValue = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(audioPlayerInteractor.currentPosition())
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(timerState = timeValue)
                delay(TIMER_UPDATE_DELAY)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }


    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }

}