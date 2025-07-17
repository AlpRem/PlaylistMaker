package com.practicum.playlistmaker.itunes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesClient {
    private const val baseUrl = "https://itunes.apple.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}