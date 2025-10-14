package com.practicum.playlistmaker.setting.domain.api

interface SettingsInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(status: Boolean)
}