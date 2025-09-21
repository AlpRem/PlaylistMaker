package com.practicum.playlistmaker.data.dto

class TracksSearchResponse(val expression: String,
                           val results: List<TrackDto>) : Response()