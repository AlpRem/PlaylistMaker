package com.practicum.playlistmaker.library.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.search.domain.model.TrackState

class PlaylistAddViewModel: ViewModel() {

    private val stateLiveData = MutableLiveData(PlaylistAddState())
    fun observeState(): LiveData<PlaylistAddState> = stateLiveData

    fun onChangedNamePlaylist(name: String?) {
        updateState { current ->
            val newName = name.orEmpty()
            current.copy(
                namePlaylist = newName,
                isAddPlaylistBtnEnabled = newName.isNotBlank(),
            )
        }
    }

    fun onChangedDescriptionPlaylist(description: String?) {
        updateState { current ->
            val newDescription = description.orEmpty()
            current.copy(
                descriptionPlaylist = newDescription,
            )
        }
    }

    fun onSelectCoverPlaylist(uri: String?) {
        updateState { current ->
            val newUri = uri.orEmpty()
            current.copy(
                coverPlaylistUri  = newUri,
            )
        }
    }

    private fun updateState(update: (PlaylistAddState) -> PlaylistAddState) {
        stateLiveData.value = update(stateLiveData.value!!)
    }
}