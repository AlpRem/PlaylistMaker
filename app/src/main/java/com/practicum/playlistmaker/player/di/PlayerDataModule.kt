package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import org.koin.dsl.module

val playerDataModule = module {

    factory { MediaPlayer() }



}