package com.practicum.playlistmaker.setting.di

import com.practicum.playlistmaker.setting.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.setting.domain.api.SettingsRepository
import org.koin.dsl.module

val settingsRepositoryModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}