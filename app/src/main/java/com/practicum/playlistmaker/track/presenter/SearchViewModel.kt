package com.practicum.playlistmaker.track.presenter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.track.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.track.domain.api.TracksInteractor
import com.practicum.playlistmaker.track.domain.model.Track
import com.practicum.playlistmaker.track.domain.model.TrackState

class SearchViewModel(private val context: Context): ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L

        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(context)
            }
        }
    }

    private val tracksInteractor: TracksInteractor = Creator.provideTracksInteractor()
    private lateinit var historyTrackInteractor: HistoryTrackInteractor

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = ""

    private val searchRunnable = Runnable {
        val newSearchText = lastQuery ?: ""
        searchRequest(newSearchText)
    }


    private val stateLiveData = MutableLiveData<TrackState>()
    fun observeState(): LiveData<TrackState> = stateLiveData


    fun searchDebounce(changedText: String) {
        if (lastQuery == changedText) {
            return
        }
        this.lastQuery = changedText

        handler.removeCallbacks(searchRunnable)
//        val postTime = SystemClock.uptimeMillis() +
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackState(page = Page.empty(), isLoading = true, isError = false, isEmpty = false))

            tracksInteractor.searchTracks(newSearchText, object: TracksInteractor.TracksConsumer {
                override fun consume(page: Page<Track>) {
                    handler.post {
                        when {
                            page.hasErrors() -> {
                                renderState(TrackState(page = Page.empty(), isLoading = false, isError = true, isEmpty = false))
                            }
                            page.isEmpty() -> {
                                renderState(TrackState(page = Page.empty(), isLoading = false, isError = false, isEmpty = true))
                            }
                            else -> {
                                renderState(TrackState(page = page, isLoading = false, isError = false, isEmpty = false))
                            }

                        }
                    }
                }
            })
        }
    }

    fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }


    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }




}