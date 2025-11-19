package com.practicum.playlistmaker.search.presenter

import android.os.Handler
import android.os.Looper
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackState

class SearchViewModel(private val tracksInteractor: TracksInteractor,
                      private val historyTrackInteractor: HistoryTrackInteractor): ViewModel() {


    private val handler: Handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = ""

    private val searchRunnable = Runnable {
        val newSearchText = lastQuery ?: ""
        searchRequest(newSearchText)
    }

    private val stateLiveData = MutableLiveData<TrackState>()
    fun observeState(): LiveData<TrackState> = stateLiveData

    private val stateOpenTrack = MutableLiveData<Track?>()
    val observeStateOpenTrack: LiveData<Track?> = stateOpenTrack

    fun searchDebounce(changedText: String) {
        if (lastQuery == changedText) {
            return
        }
        this.lastQuery = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onSearchFocusChange(hasFocus: Boolean, searchEditText: Editable?) {
        if (hasFocus && searchEditText.isNullOrEmpty())
            loadHistory()

    }

    fun onOpenAudioPlayer(track: Track) {
        saveToHistory(track)
        stateOpenTrack.postValue(track)
    }

    fun clearHistory() {
        historyTrackInteractor.clearHistory()
        renderState(TrackState.empty())
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackState.loading())

            tracksInteractor.searchTracks(newSearchText, object: TracksInteractor.TracksConsumer {
                override fun consume(page: Page<Track>) {
                    handler.post {
                        when {
                            page.hasErrors() -> {
                                renderState(TrackState.error())
                            }
                            page.isEmpty() -> {
                                renderState(TrackState.empty())
                            }
                            else -> renderState(TrackState.content(page))
                        }
                    }
                }
            })
        } else
            loadHistory()
    }

    private fun loadHistory() {
        historyTrackInteractor.getHistory { page ->
            handler.post {
                if (page.data.isNotEmpty()) {
                    renderState(TrackState.content(page, isHistory = true))
                }
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


    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}