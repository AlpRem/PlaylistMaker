package com.practicum.playlistmaker.library.presenter

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.library.domain.api.ImageStorageInteractor
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistAddState
import com.practicum.playlistmaker.search.domain.model.TrackState
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class PlaylistAddViewModel(
    private val playlistDbInteractor: PlaylistDbInteractor,
    private val imageStorageInteractor: ImageStorageInteractor): ViewModel() {

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

    fun savePlaylist() {
        val state = stateLiveData.value ?: return
        if (state.namePlaylist.isBlank()) return

        viewModelScope.launch {
            val imagePath = if (!state.coverPlaylistUri.isNullOrBlank()) {
                imageStorageInteractor.saveImage(state.coverPlaylistUri.toUri())
            } else {
                ""
            }

            val playlist = Playlist(
                id = 0,
                name = state.namePlaylist,
                description = state.descriptionPlaylist.orEmpty(),
                image = imagePath,
                tracksIds = "[]",
                countTracks = 0
            )
            playlistDbInteractor.save(playlist)
        }
    }
}