package com.practicum.playlistmaker.setting.domain.api

interface SettingsRepository {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(status: Boolean)
}