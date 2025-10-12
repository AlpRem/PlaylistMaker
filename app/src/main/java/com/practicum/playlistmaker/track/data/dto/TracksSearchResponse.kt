package com.practicum.playlistmaker.track.data.dto

class TracksSearchResponse(val expression: String,
                           val results: List<TrackDto>) : Response()