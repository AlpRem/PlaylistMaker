package com.practicum.playlistmaker.track.data.network

import com.practicum.playlistmaker.track.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search")
    fun search(@Query("term") term: String,
               @Query("entity") entityType: String = "song"
    ): Call<TracksSearchResponse>
}