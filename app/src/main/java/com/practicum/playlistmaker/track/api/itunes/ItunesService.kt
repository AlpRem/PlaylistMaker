package com.practicum.playlistmaker.track.api.itunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search")
    fun search(@Query("term") term: String,
               @Query("entity") entityType: String = "song"
    ): Call<ITunesResponse>
}