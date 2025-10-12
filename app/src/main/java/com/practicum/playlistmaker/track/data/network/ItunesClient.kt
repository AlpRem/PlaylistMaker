package com.practicum.playlistmaker.track.data.network

import com.practicum.playlistmaker.track.data.network.NetworkClient
import com.practicum.playlistmaker.track.data.dto.Response
import com.practicum.playlistmaker.track.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesClient : NetworkClient {
    private val BASE_URL = "https://itunes.apple.com"


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = itunesService.search(dto.query).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }

        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}