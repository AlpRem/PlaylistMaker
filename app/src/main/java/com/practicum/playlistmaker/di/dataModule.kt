package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.search.data.mapper.TrackMapper
import com.practicum.playlistmaker.search.data.network.ItunesClient
import com.practicum.playlistmaker.search.data.network.ItunesService
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    factory { MediaPlayer() }
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

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

}
