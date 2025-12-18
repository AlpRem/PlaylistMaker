package com.practicum.playlistmaker.search.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.api.HistoryTrackInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class HistoryTrackInteractorImpl(private val repository: HistoryTrackRepository): HistoryTrackInteractor {

    override fun getHistory(): Flow<Page<Track>>  {
        return repository.getHistory()
    }

    override fun saveTrack(track: Track) {
        repository.setHistory(track)
    }

    override fun clearHistory() {
        repository.cleanHistory()
    }
}