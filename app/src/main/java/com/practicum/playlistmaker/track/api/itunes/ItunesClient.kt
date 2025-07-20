package com.practicum.playlistmaker.track.api.itunes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesClient {
    private const val BASE_URL = "https://itunes.apple.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val itunesService: ItunesService = retrofit.create(ItunesService::class.java)
}