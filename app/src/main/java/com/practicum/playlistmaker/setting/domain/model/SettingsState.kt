package com.practicum.playlistmaker.setting.domain.model

data class SettingsState(
    val isDarkTheme: Boolean = false
) {
    companion object {
        fun setTheme(isDarkTheme: Boolean) = SettingsState(
            isDarkTheme = isDarkTheme
        )
    }
}