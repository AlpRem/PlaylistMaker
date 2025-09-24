package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository

class SettingsInteractorImpl( private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun isDarkThemeEnabled() = settingsRepository.isDarkThemeEnabled()
    override fun setDarkThemeEnabled(status: Boolean) = settingsRepository.setDarkThemeEnabled(status)
}