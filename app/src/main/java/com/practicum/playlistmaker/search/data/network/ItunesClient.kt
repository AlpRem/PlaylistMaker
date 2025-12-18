package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesClient(private val itunesService: ItunesService) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val body = itunesService.search(dto.query)
                body.apply { resultCode = 200 }
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = 500 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}