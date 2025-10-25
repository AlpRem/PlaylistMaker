package com.practicum.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.data.network.ItunesClient
import com.practicum.playlistmaker.search.data.network.ItunesService
import com.practicum.playlistmaker.search.data.network.NetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    single<ItunesService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(
                PLAYLIST_MAKER_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    single  { Gson() }

    single { TrackMapper() }

    single<NetworkClient> {
        ItunesClient(get())
    }
}