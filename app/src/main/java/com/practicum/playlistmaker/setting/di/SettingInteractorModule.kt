package com.practicum.playlistmaker.setting.di


import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.setting.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val settingInteractorModule = module {
    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}