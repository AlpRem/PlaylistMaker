package com.practicum.playlistmaker.player.domain.util

import com.practicum.playlistmaker.search.domain.model.Track

class PlaylistToText {
    fun build(
        name: String,
        description: String,
        countTrack: String,
        tracks: List<Track>
    ): String {
        return buildString {
            appendLine(name)
            if (description.isNotBlank())
                appendLine(description)
            appendLine(countTrack)
            appendLine()
            tracks.forEachIndexed { index, track ->
                appendLine(
                    "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})"
                )
            }
        }
    }
}