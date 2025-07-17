package com.practicum.playlistmaker.itunes

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search")
    fun search(@Query("term") term: String,
               @Query("entity") entityType: String = "song"
    ): Call<ITunesResponse>
}