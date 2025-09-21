package com.practicum.playlistmaker.track.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.HISTORY_TRACKS
import com.practicum.playlistmaker.component.Page
import com.practicum.playlistmaker.domain.model.Track
import androidx.core.content.edit
import com.practicum.playlistmaker.component.Meta


class HistoryTrackRepositoryImpl(): HistoryTrackRepository {

    override fun getHistory(sharedPreferences: SharedPreferences, callback: (Page<Track>) -> Unit) {
        val tracks = readTrack(sharedPreferences)
        callback(Page(
            meta = Meta(
                count = tracks.size,
                errors = emptyList()
            ),
            data = tracks.reversed()
            )
        )
    }

    override fun setHistory(sharedPreferences: SharedPreferences, track: Track) {
        sharedPreferences.edit {
            putString(HISTORY_TRACKS, writeTrack(sharedPreferences, track))
        }
    }

    override fun cleanHistory(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit {
            remove(HISTORY_TRACKS)
        }
    }

    private fun writeTrack(sharedPreferences: SharedPreferences, track: Track): String {
        val trackList = readTrack(sharedPreferences).toMutableList();
        trackList.removeAll {it.trackId == track.trackId}
        if (trackList.size >= 10)
            trackList.removeAt(0);
        trackList.add(track);
        return Gson().toJson(trackList.toList())

    }

    private fun readTrack(sharedPreferences: SharedPreferences): Collection<Track> {
        val json = sharedPreferences.getString(HISTORY_TRACKS, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }
}