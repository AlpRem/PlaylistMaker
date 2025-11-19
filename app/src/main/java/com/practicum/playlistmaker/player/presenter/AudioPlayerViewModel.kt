package com.practicum.playlistmaker.player.presenter

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.AudioPlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor): ViewModel() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { setTimerValueRunnable() }

    private val stateAudioPlayer = MutableLiveData<AudioPlayerState>(AudioPlayerState.Default)
    val observeStateAudioPlayer: LiveData<AudioPlayerState> = stateAudioPlayer

    private val stateTimer = MutableLiveData("00:00")
    val observeTimer: LiveData<String> = stateTimer

    var track: Track? = null
        private set


    private fun setTimerValueRunnable() {
        stateTimer.postValue(SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(audioPlayerInteractor.currentPosition()))
        if (audioPlayerInteractor.isPlaying()) {
            handler.postDelayed(timerRunnable,TIMER_UPDATE_DELAY)
        }
    }


    fun preparePlayer(track: Track) {
        this.track = track
        audioPlayerInteractor.preparePlayer(track,
            onPrepared = {
                stateAudioPlayer.postValue(AudioPlayerState.Prepared)
            },
            onCompletion = {
                stateAudioPlayer.postValue(AudioPlayerState.Prepared)
                stateTimer.postValue("00:00")
                handler.removeCallbacks(timerRunnable)
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
            handler.postDelayed(timerRunnable, TIMER_UPDATE_DELAY)
        })
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer(onPause = {
            stateAudioPlayer.postValue(AudioPlayerState.Paused)
            handler.removeCallbacks(timerRunnable)
        })
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }

}