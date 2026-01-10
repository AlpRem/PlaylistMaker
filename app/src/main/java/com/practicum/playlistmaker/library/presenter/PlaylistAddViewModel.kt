package com.practicum.playlistmaker.library.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.search.domain.model.TrackState

class PlaylistAddViewModel: ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistAddState>(
        PlaylistAddState(isAddPlaylistBtnEnabled = false)
    )
    fun observeState(): LiveData<PlaylistAddState> = stateLiveData

    fun onChangedNamePlaylist(name: String?) {
        stateLiveData.value = PlaylistAddState(
            isAddPlaylistBtnEnabled = !name.isNullOrBlank()
        )
    }
}