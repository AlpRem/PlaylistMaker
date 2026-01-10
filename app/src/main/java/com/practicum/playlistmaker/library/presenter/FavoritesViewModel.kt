package com.practicum.playlistmaker.library.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.TrackDbInteractor
import com.practicum.playlistmaker.library.domain.model.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val trackDbInteractor: TrackDbInteractor): ViewModel() {
    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    init {
        fillData()
    }

    private fun fillData() {
        renderState(FavoritesState.loading())
        viewModelScope.launch {
            trackDbInteractor.list().collect { page ->
                when {
                    page.isEmpty() -> renderState(FavoritesState.empty())
                    else -> renderState(FavoritesState.content(page))
                }
            }
        }
    }

    private fun renderState(state: FavoritesState) {
        stateLiveData.postValue(state)
    }

}