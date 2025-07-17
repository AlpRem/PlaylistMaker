package com.practicum.playlistmaker.itunes

import com.practicum.playlistmaker.track.model.Track
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search")
    fun search(@Query("term") term: String, @Query("entity") entityType: String = "song"): Track
}