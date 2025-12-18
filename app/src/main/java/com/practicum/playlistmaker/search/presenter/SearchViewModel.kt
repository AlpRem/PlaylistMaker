package com.practicum.playlistmaker.search.presenter

import android.os.Handler
import android.os.Looper
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor,
                      private val historyTrackInteractor: HistoryTrackInteractor): ViewModel() {

    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<TrackState>()
    fun observeState(): LiveData<TrackState> = stateLiveData

    private val stateOpenTrack = MutableLiveData<Track?>()
    val observeStateOpenTrack: LiveData<Track?> = stateOpenTrack

    fun searchDebounce(changedText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun onSearchFocusChange(hasFocus: Boolean, searchEditText: Editable?) {
        if (hasFocus && searchEditText.isNullOrEmpty())
            loadHistory()

    }

    fun onOpenAudioPlayer(track: Track) {
        saveToHistory(track)
        stateOpenTrack.value = track
    }

    fun clearHistory() {
        historyTrackInteractor.clearHistory()
        renderState(TrackState.empty())
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackState.loading())
            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText).collect { page ->
                    when {
                        page.isEmpty() -> renderState(TrackState.empty())
                        page.hasErrors() -> renderState(TrackState.error())
                        else -> renderState(TrackState.content(page))
                    }
                }
            }
        } else
            loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            historyTrackInteractor.getHistory().collect { page ->
                if (page.data.isNotEmpty())
                    renderState(TrackState.content(page, isHistory = true))
            }
        }
    }

    private fun saveToHistory(track: Track) {
        historyTrackInteractor.saveTrack(track)
    }

    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }

    fun onSearchCleared () {
        stateLiveData.postValue(TrackState.clear())
    }

    fun resetOpenTrackState() {
        stateOpenTrack.value = null
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}