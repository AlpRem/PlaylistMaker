package com.practicum.playlistmaker.library.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.library.domain.model.PlaylistState
import com.practicum.playlistmaker.setting.ui.SettingFragment.Companion.TAG
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistDbInteractor: PlaylistDbInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData
    fun listPlaylist() {
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

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }
}