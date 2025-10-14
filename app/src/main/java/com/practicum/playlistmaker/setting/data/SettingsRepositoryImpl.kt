package com.practicum.playlistmaker.setting.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker.IS_DARK_THEME
import com.practicum.playlistmaker.setting.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences): SettingsRepository {

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPrefs.getBoolean(IS_DARK_THEME, false)
    }

    override fun setDarkThemeEnabled(status: Boolean) {
        sharedPrefs.edit { putBoolean(IS_DARK_THEME, status) }
    }
}