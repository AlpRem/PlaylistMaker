package com.practicum.playlistmaker.sharing.di

import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingInteractorModule = module {
    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}