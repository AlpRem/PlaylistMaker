package com.practicum.playlistmaker.library.presenter

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.library.domain.model.PlaylistDetailsState
import com.practicum.playlistmaker.player.domain.util.PlaylistToText
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.R
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistDbInteractor: PlaylistDbInteractor,
    private val playlistToText: PlaylistToText,
    private val sharingInteractor: SharingInteractor,
    private val resources: Resources): ViewModel() {

    private val stateLiveData = MutableLiveData(PlaylistDetailsState())
    fun observeState(): LiveData<PlaylistDetailsState> = stateLiveData

    fun loadPlaylist(id: Long) {
        stateLiveData.value = stateLiveData.value?.copy(
            isLoading = true, isEmpty = false)
        viewModelScope.launch {
            val playlistDetails = playlistDbInteractor.findById(id)

            stateLiveData.value = PlaylistDetailsState(
                isLoading = false,
                playlist = playlistDetails?.playlist,
                tracks = playlistDetails?.tracks.orEmpty(),
                totalDurationMillis = playlistDetails?.totalDurationMillis ?: 0L,
                isEmpty = playlistDetails == null
            )
        }
    }

    fun onOpenAudioPlayer(track: Track) {
        val currentState = stateLiveData.value ?: PlaylistDetailsState()
        stateLiveData.value = currentState.copy(playerTrack = track)
    }

    fun delete(track: Track) {
        val playlistId = stateLiveData.value?.playlist?.id ?: return
        viewModelScope.launch {
            playlistDbInteractor.deleteTrackToPlaylist(playlistId, track)
            loadPlaylist(playlistId)
        }
    }

    fun delete() {
        val state = stateLiveData.value ?: return
        val playlistId = state.playlist?.id ?: return
        viewModelScope.launch {
            playlistDbInteractor.deletePlaylist(playlistId, state.tracks)
            stateLiveData.postValue(
                state.copy(completerDelete = true)
            )
        }
    }

    fun changeFlagCompleterDelete() {
        val state = stateLiveData.value ?: return
        if (state.completerDelete)
            stateLiveData.value = state.copy(completerDelete = false)
    }

    fun resetOpenTrack() {
        val currentState = stateLiveData.value ?: return
        if (currentState.playerTrack != null) {
            stateLiveData.value = currentState.copy(playerTrack = null)
        }
    }

    fun shareApp() {
        val state = stateLiveData.value ?: return
        val playlist = state.playlist ?: return
        val countTrack = resources.getQuantityString(
            R.plurals.tracks_count,
            state.tracks.size,
            state.tracks.size
        )
        val playlistInfo = playlistToText.build(
            name = playlist.name,
            description = playlist.description,
            countTrack = countTrack,
            tracks = state.tracks
        )
        sharingInteractor.sharePlaylistApp(playlistInfo)
    }}