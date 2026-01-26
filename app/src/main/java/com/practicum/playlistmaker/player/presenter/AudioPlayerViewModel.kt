package com.practicum.playlistmaker.player.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.db.domain.api.TrackDbInteractor
import com.practicum.playlistmaker.db.domain.model.AddTrackToPlaylistResult
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistState
import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.model.AddTrackToPlaylistState
import com.practicum.playlistmaker.player.domain.model.AudioPlayerFragmentState
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor,
    private val trackDbInteractor: TrackDbInteractor,
    private val playlistDbInteractor: PlaylistDbInteractor): ViewModel() {
    private val stateAudioPlayer = MutableLiveData(AudioPlayerFragmentState())
    val observeStateAudioPlayer: LiveData<AudioPlayerFragmentState> = stateAudioPlayer

    var track: Track? = null
        private set


    private var timerJob: Job? = null

    fun preparePlayer(track: Track) {
        this.track = track
        stateAudioPlayer.value = stateAudioPlayer.value?.copy(audioPlayerState =
            stateAudioPlayer.value!!.audioPlayerState.copy (likeState = track.isFavorite))
        audioPlayerInteractor.preparePlayer(track,
            onPrepared = {
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(audioPlayerState =
                    stateAudioPlayer.value!!.audioPlayerState.copy(playerState = PlayerState.Prepared))

            },
            onCompletion = {
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(audioPlayerState =
                    stateAudioPlayer.value!!.audioPlayerState.copy(playerState = PlayerState.Prepared))
                stopTimer()
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(audioPlayerState =
                    stateAudioPlayer.value!!.audioPlayerState.copy(timerState = "00:00"))
            })
    }

    fun playbackControl() {
        when (stateAudioPlayer.value?.audioPlayerState?.playerState){
            PlayerState.Playing -> pausePlayer()
            else -> startPlayer()
        }
    }

    fun onPause() {
        pausePlayer()
    }

    fun getPlaylist() {
        renderState(PlaylistState.loading())

        viewModelScope.launch {
            playlistDbInteractor
                .list()
                .collect { page ->
                    when {
                        page.hasErrors() -> renderState(PlaylistState.error())
                        page.isEmpty() -> renderState(PlaylistState.empty())
                        else -> {
                            renderState(PlaylistState.content(page))
                        }
                    }
                }
        }
    }

    fun toggleLike() {
        val currentTrack = track ?: return
        viewModelScope.launch {
            val newLikeStatus = !currentTrack.isFavorite
            val newTrack = currentTrack.copy(isFavorite = newLikeStatus)
            trackDbInteractor.save(newTrack)
            track = newTrack
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(
                stateAudioPlayer.value!!.audioPlayerState.copy(likeState = newLikeStatus))
        }
    }

    fun addPlaylist(playlist: Playlist) {
        val currentTrack = track ?: return
        viewModelScope.launch {
            val result = playlistDbInteractor.addTrackToPlaylist(
                playlistId = playlist.id,
                track = currentTrack
            )
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(
                addTrackState = when (result) {
                    AddTrackToPlaylistResult.ToAdded ->
                        AddTrackToPlaylistState.added(playlist.name)
                    AddTrackToPlaylistResult.TrackIsExists ->
                        AddTrackToPlaylistState.exists(playlist.name)
                }
            )
        }
    }

    fun clearAddTrackState() {
        stateAudioPlayer.value = stateAudioPlayer.value?.copy(
            addTrackState = AddTrackToPlaylistState.clear()
        )
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer(onStart = {
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(
                stateAudioPlayer.value!!.audioPlayerState.copy(playerState = PlayerState.Playing))
            startTimer()
        })
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer(onPause = {
            stateAudioPlayer.value = stateAudioPlayer.value?.copy(
                stateAudioPlayer.value!!.audioPlayerState.copy(playerState = PlayerState.Paused))
            stopTimer()
        })
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.isPlaying()) {
                val timeValue = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(audioPlayerInteractor.currentPosition())
                stateAudioPlayer.value = stateAudioPlayer.value?.copy(
                    stateAudioPlayer.value!!.audioPlayerState.copy(timerState = timeValue))
                delay(TIMER_UPDATE_DELAY)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun renderState(state: PlaylistState) {
        stateAudioPlayer.value = stateAudioPlayer.value?.copy(
            playlistState = state
        )
    }


    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }

}