package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.HISTORY_TRACKS
import com.practicum.playlistmaker.common.component.Page
import com.practicum.playlistmaker.search.domain.api.HistoryTrackRepository
import com.practicum.playlistmaker.search.domain.model.Track

class HistoryTrackRepositoryImpl(private val sharedPreferences: SharedPreferences,
                                 private val gson: Gson
): HistoryTrackRepository {

    override fun getHistory(): Page<Track> {
        val tracks = readTrack()
        return Page.Companion.of(tracks.reversed())
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