package com.practicum.playlistmaker.search.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.search.domain.model.Track

class HistoryTrackInteractorImpl(private val repository: HistoryTrackRepository): HistoryTrackInteractor {

    private val handler = Handler(Looper.getMainLooper())

    override fun getHistory(consumer: (Page<Track>) -> Unit) {
        Thread {
            val result = repository.getHistory()
            handler.post { consumer(result) }
        }.start()
    }

    override fun saveTrack(track: Track) {
        Thread {
            repository.setHistory(track)
        }.start()
    }

    override fun clearHistory() {
        Thread {
            repository.cleanHistory()
        }.start()
    }
}