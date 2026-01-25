package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.HISTORY_TRACKS
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.db.data.AppDatabase
import com.practicum.playlistmaker.search.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class HistoryTrackRepositoryImpl(private val sharedPreferences: SharedPreferences,
                                 private val gson: Gson,
                                 private val appDatabase: AppDatabase
): HistoryTrackRepository {

    override fun getHistory(): Flow<Page<Track>> = combine(
        flow {
            emit(readTrack())
             },
        appDatabase.trackDao().findByFavoriteAllIds()) {
                                             historyTracks, favoriteIds ->
        val favoriteSet = favoriteIds.toSet()
        val updatedTracks = historyTracks
            .map { track -> track.copy(isFavorite = favoriteSet.contains(track.trackId))}
            .reversed()
        Page.of(updatedTracks)
    }

    override fun setHistory(track: Track) {
        sharedPreferences.edit {
            putString(HISTORY_TRACKS, writeTrack(track))
        }
    }

    override fun cleanHistory() {
        sharedPreferences.edit {
            remove(HISTORY_TRACKS)
        }
    }

    private fun writeTrack(track: Track): String {
        val trackList = readTrack().toMutableList()
        trackList.removeAll { it.trackId == track.trackId }
        if (trackList.size >= 10) {
            trackList.removeAt(0)
        }
        trackList.add(track)
        return gson.toJson(trackList)

    }

    private fun readTrack(): List<Track> {
        val json = sharedPreferences.getString(HISTORY_TRACKS, null) ?: return emptyList()
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }
}