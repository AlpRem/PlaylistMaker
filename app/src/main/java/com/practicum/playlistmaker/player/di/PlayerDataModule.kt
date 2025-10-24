package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import org.koin.dsl.module

val playerDataModule = module {

    factory { MediaPlayer() }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

}