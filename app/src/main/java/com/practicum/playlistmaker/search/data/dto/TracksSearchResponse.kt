package com.practicum.playlistmaker.search.data.dto

class TracksSearchResponse(val expression: String,
                           val results: List<TrackDto>) : Response()