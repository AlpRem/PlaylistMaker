package com.practicum.playlistmaker.library.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.library.domain.model.PlaylistDetailsState
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistDbInteractor: PlaylistDbInteractor): ViewModel() {

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
                totalDurationMillis = playlistDetails?.totalDurationMillis ?: 0L,
                isEmpty = playlistDetails == null
            )
        }
    }
}