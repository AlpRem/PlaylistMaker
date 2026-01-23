package com.practicum.playlistmaker.library.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.library.domain.model.Playlist
import com.practicum.playlistmaker.library.domain.model.PlaylistState
import com.practicum.playlistmaker.setting.ui.SettingFragment.Companion.TAG
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistDbInteractor: PlaylistDbInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData
    private var currentPage: Page<Playlist> = Page.empty()
    fun listPlaylist() {
        renderState(PlaylistState.loading())

        viewModelScope.launch {
            playlistDbInteractor
                .list()
                .collect { page ->
                    currentPage = page
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

    fun onPlaylistClicked(playlist: Playlist) {
        renderState(
            PlaylistState.openPlaylist(
                page = currentPage,
                playlistId = playlist.id
            )
        )
    }

    fun clearOpenPlaylist() {
        renderState(PlaylistState.content(currentPage))
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }
}