package com.practicum.playlistmaker.setting.domain.impl

import com.practicum.playlistmaker.setting.domain.api.SettingsInteractor
import com.practicum.playlistmaker.setting.domain.api.SettingsRepository

class SettingsInteractorImpl( private val settingsRepository: SettingsRepository):
    SettingsInteractor {
    override fun isDarkThemeEnabled() = settingsRepository.isDarkThemeEnabled()
    override fun setDarkThemeEnabled(status: Boolean) = settingsRepository.setDarkThemeEnabled(status)
}