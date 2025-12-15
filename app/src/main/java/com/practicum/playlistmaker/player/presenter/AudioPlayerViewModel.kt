package com.practicum.playlistmaker.player.presenter

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor): ViewModel() {

    private val stateAudioPlayer = MutableLiveData<AudioPlayerState>(AudioPlayerState.Default)
    val observeStateAudioPlayer: LiveData<AudioPlayerState> = stateAudioPlayer

    private val stateTimer = MutableLiveData("00:00")
    val observeTimer: LiveData<String> = stateTimer

    var track: Track? = null
        private set


    private var timerJob: Job? = null

    fun preparePlayer(track: Track) {
        this.track = track
        audioPlayerInteractor.preparePlayer(track,
            onPrepared = {
                stateAudioPlayer.postValue(AudioPlayerState.Prepared)
            },
            onCompletion = {
                stateAudioPlayer.postValue(AudioPlayerState.Prepared)
                stopTimer()
                stateTimer.postValue("00:00")
            })
    }

    fun playbackControl() {
        when (stateAudioPlayer.value){
            AudioPlayerState.Playing -> pausePlayer()
            else -> startPlayer()
        }
    }

    fun onPause() {
        pausePlayer()
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer(onStart = {
            stateAudioPlayer.postValue(AudioPlayerState.Playing)
            startTimer()
        })
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer(onPause = {
            stateAudioPlayer.postValue(AudioPlayerState.Paused)
            stopTimer()
        })
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.isPlaying()) {
                stateTimer.postValue(
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(audioPlayerInteractor.currentPosition())
                )
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